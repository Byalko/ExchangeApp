package com.example.exchangeapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.db.CurrencyDao
import com.example.exchangeapp.db.CurrencyDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val currencyDao: CurrencyDao
): ViewModel() {

    private val _data = MutableStateFlow<ExchangeEvent>(ExchangeEvent.Empty)
    val data: StateFlow<ExchangeEvent> = _data

    init {
        viewModelScope.launch {
            _data.value = ExchangeEvent.Loading
            currencyDao.getAll().collect {
                if (it.isEmpty()){
                    _data.value = ExchangeEvent.Empty
                } else {
                    _data.value = ExchangeEvent.Success(it)
                }
            }
        }
    }

    fun deleteCurrency(name: String, baseName: String) {
        viewModelScope.launch(Dispatchers.IO){
            val currencyFromDb = currencyDao.getCurrencyByKeys(name, baseName)
            currencyDao.delete(currencyFromDb)
        }
    }
}

sealed class ExchangeEvent {
    class Success(val result: List<CurrencyDto>) : ExchangeEvent()
    object Loading : ExchangeEvent()
    object Empty : ExchangeEvent()
}
