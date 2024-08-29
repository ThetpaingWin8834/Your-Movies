package com.ym.yourmovies.cm.home.models

data class CmMovie(
    val title:String,
    val description:String="",
    val rating:String,
    val thumb:String,
    val url:String,
)