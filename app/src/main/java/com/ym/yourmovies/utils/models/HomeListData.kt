package com.ym.yourmovies.utils.models

 class HomeListData<T>(
    val data :NameAndUrlModel = NameAndUrlModel() ,
    val list: List<T> = emptyList()
)