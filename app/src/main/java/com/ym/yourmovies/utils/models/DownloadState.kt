package com.ym.yourmovies.utils.models

data class DownloadState(
    val isLoading :Boolean = true,
    val error:Exception?=null,
    val generatedLink: String = ""
)