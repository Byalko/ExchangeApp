package com.example.exchangeapp.data

import com.example.exchangeapp.util.Result
import com.example.exchangeapp.domain.DataFetchError
import com.haroldadmin.cnradapter.NetworkResponse
import java.io.EOFException
import java.net.HttpURLConnection

fun <Response : Any> NetworkResponse<Response, ApiError>.toResult(): Result<Response, DataFetchError> =
    toResult { it }

fun <Response : Any, Output : Any> NetworkResponse<Response, ApiError>.toResult(
    resultMapper: (Response) -> Output
): Result<Output, DataFetchError> = when (this) {
    is NetworkResponse.Success -> {
        Result.success(resultMapper(body))
    }
    is NetworkResponse.ServerError -> {
        if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            Result.error(DataFetchError.Unauthorized(error))
        } else {
            Result.error(DataFetchError.ServerError(code, error))
        }
    }
    is NetworkResponse.NetworkError -> {
        if (isServerError()) {
            Result.error(DataFetchError.ServerError(-1, error))
        } else {
            Result.error(DataFetchError.ConnectionError(error))
        }
    }
    is NetworkResponse.UnknownError -> {
        if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            Result.error(DataFetchError.Unauthorized(error))
        } else {
            Result.error(DataFetchError.ServerError(code ?: -1, error))
        }
    }
}

private fun NetworkResponse.NetworkError.isServerError(): Boolean =
    error is EOFException
