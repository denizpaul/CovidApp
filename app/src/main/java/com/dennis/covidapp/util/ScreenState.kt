package com.dennis.covidapp.util

sealed class ScreenState<T>(
    val data: T? =null,
    val errorMessage: String? = null
) {
    class Success<T>(data: T): ScreenState<T>(data)
    class Error<T>(message: String, data: T?=null): ScreenState<T>(data, message)
    class Loading<T>(): ScreenState<T>()
}