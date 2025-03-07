package com.chami.strippaymenttest.data.model

sealed class ResultStatus<out T> {
    object Loading : ResultStatus<Nothing>()
    data class Success<out T>(val data: T) : ResultStatus<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : ResultStatus<Nothing>()
}