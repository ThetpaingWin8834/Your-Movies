package com.ym.yourmovies.msub.model

data class YoutubeDownloadModel(
    val cipher: Boolean,
    val id: String,
    val meta: Meta,
    val thumb: String,
    val url: List<Url>
)