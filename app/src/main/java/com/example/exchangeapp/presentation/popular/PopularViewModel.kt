package com.example.exchangeapp.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.db.CurrencyDao
import com.example.exchangeapp.db.CurrencyDto
import com.example.exchangeapp.domain.ExchangeRepository
import com.example.exchangeapp.util.onError
import com.example.exchangeapp.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
    private val currencyDao: CurrencyDao
) : ViewModel() {

    private val _data = MutableStateFlow<ExchangeEvent>(ExchangeEvent.Empty)
    val data: StateFlow<ExchangeEvent> = _data

    init {
        getExchangeData()
    }

    fun getExchangeData(baseCurrency: String = "USD") {
        viewModelScope.launch {
            _data.emit(ExchangeEvent.Loading)
            exchangeRepository.getExchangeRatesByCurrency(baseCurrency).onSuccess { exchange ->
                val currencies = mutableListOf<Currency>()
                exchange.conversion_rates.entries.filter { it.key != baseCurrency }
                    .map { currencies.add(Currency(it.key, it.value)) }
                _data.emit(
                    ExchangeEvent.Success(Currencies(exchange.base_code, currencies.toList()))
                )
            }.onError {
                _data.emit(ExchangeEvent.Failure(it.message.toString()))
            }
        }
    }

    fun saveCurrency(name: String, value: Double) {
        if (_data.value is ExchangeEvent.Success) {
            val baseCode = (_data.value as ExchangeEvent.Success).result.baseName
            viewModelScope.launch(Dispatchers.IO) {
                currencyDao.insert(CurrencyDto(name, value, baseCode))
            }
        }
    }

    fun sortList(it: Int) {
        if (_data.value is ExchangeEvent.Success) {
            val result = (_data.value as ExchangeEvent.Success).result
            val sort = when(it){
                0,1 -> result.currencies.sortedBy { it.name }
                else -> result.currencies.sortedBy { it.value }.asReversed()
            }
            viewModelScope.launch {
                _data.emit(ExchangeEvent.Success(Currencies(result.baseName, sort)))
            }
        }
    }
}

sealed class ExchangeEvent {
    class Success(val result: Currencies) : ExchangeEvent()
    class Failure(val errorText: String) : ExchangeEvent()
    object Loading : ExchangeEvent()
    object Empty : ExchangeEvent()
}

data class Currencies(val baseName: String, val currencies: List<Currency>)
data class Currency(val name: String, val value: Double)