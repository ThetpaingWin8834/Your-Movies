package com.ym.yourmovies.gc.details.models

data class GcSeriesDownloadData(
    val header: GcSeriesDownloadHeader,
    val downloadList : List<GcSeriesDownloadDataItem>
)

data class GcSeriesDownloadHeader(
    val seasonIndex : String,
    val title : String,
    val date : String,
)
data class GcSeriesDownloadDataItem(
    val thumb:String,
    val title:String,
    val episode:String,
    val date:String,
    val url:String
)