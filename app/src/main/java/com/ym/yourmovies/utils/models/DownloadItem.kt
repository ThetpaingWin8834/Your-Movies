package com.ym.yourmovies.utils.models

data class DownloadItem(
    val title :String ,
    val thumb :String ,
    val server :String ,
    val serverIcon :String ,
    val more : List<String>,
    val url :String
)