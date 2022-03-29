package com.example.exchangeapp.domain

sealed class DataFetchError : Throwable() {
    data class ConnectionError(override val cause: Throwable? = null) : DataFetchError()
    data class ServerError(val errorCode: Int, override val cause: Throwable? = null) : DataFetchError()
    data class Unauthorized(override val cause: Throwable? = null) : DataFetchError()
}
