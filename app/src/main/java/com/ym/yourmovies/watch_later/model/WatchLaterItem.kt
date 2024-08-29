package com.ym.yourmovies.watch_later.model

import com.ym.yourmovies.utils.models.Channel


data class WatchLaterItem(
    val id :Int,
    val title:String,
    val rating:String,
    val date:String,
    val thumb:String,
    val url:String,
    val channel: Channel,
    val isSelected : Boolean = false
)