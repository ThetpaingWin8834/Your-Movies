package com.ym.yourmovies.msub.model

data class Url(
    val ytVideoAttr: YtVideoAttr,
    val audioCodec: String?,
    val contentLength: Int,
    val downloadable: Boolean,
    val filesize: Int,
    val no_audio: Boolean,
    val audio: Boolean,
    val quality: String,
    val url: String,
    val videoCodec: String?
)