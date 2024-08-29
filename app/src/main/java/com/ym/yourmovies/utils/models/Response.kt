package com.ym.yourmovies.utils.models

sealed interface Response<T> {
    class Success<T>(val data:T): Response<T>
    class Error<T>(val exception: Exception) : Response<T>
    class Loading<T>: Response<T>
}