package com.example.exchangeapp.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.data.model.Exchange
import com.example.exchangeapp.domain.ExchangeRepository
import com.example.exchangeapp.util.onError
import com.example.exchangeapp.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepository
): ViewModel() {

    private val _data = MutableStateFlow<ExchangeEvent>(ExchangeEvent.Empty)
    val data: StateFlow<ExchangeEvent> = _data

    fun getExchangeData(currency: String = "USD") {
        viewModelScope.launch {
            exchangeRepository.getExchangeRatesByCurrency(currency).
            onSuccess {
                _data.emit(ExchangeEvent.Success(it))
            }.onError {
                _data.emit(ExchangeEvent.Failure(it.message.toString()))
            }
        }
    }
}

sealed class ExchangeEvent {
    class Success(val result: Exchange) : ExchangeEvent()
    class Failure(val errorText: String) : ExchangeEvent()
    object Loading : ExchangeEvent()
    object Empty : ExchangeEvent()
}