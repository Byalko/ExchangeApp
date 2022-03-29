package com.example.exchangeapp.data.repository

import com.example.exchangeapp.data.toResult
import com.example.exchangeapp.domain.DataFetchError
import com.example.exchangeapp.data.ExchangeRateApi
import com.example.exchangeapp.data.model.Exchange
import com.example.exchangeapp.domain.ExchangeRepository
import com.example.exchangeapp.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi
): ExchangeRepository {
    override suspend fun getExchangeRatesByCurrency(currency: String): Result<Exchange, DataFetchError> =
        withContext(Dispatchers.IO) {
            api.getExchangeRatesByCurrency(currency).toResult()
        }
}