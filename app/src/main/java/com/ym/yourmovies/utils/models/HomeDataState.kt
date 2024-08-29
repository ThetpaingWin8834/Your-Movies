package com.ym.yourmovies.utils.models

data class HomeDataState<Data>(
    val isLoading :Boolean = false,
    val error : Exception?=null,
    val data:Data
)