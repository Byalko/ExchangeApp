package com.example.exchangeapp.domain

import com.example.exchangeapp.data.model.Exchange
import com.example.exchangeapp.util.Result

interface ExchangeRepository {
    suspend fun getExchangeRatesByCurrency(currency: String): Result<Exchange, DataFetchError>
}