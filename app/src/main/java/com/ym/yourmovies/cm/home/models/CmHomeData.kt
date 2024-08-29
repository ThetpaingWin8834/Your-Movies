package com.ym.yourmovies.cm.home.models


data class CmHomeData(
    val headerList: List<CmHeaderData> = emptyList(),
    val movieList: List<CmMovie> = emptyList(),
    val tvList: List<CmMovie> = emptyList(),
)