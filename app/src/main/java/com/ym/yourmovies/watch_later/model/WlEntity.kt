package com.ym.yourmovies.watch_later.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ym_wl")
data class WlEntity(
    @PrimaryKey(autoGenerate = true) val id :Int ,
    val title:String,
    val rating:String,
    val date:String,
    val thumb:String,
    val url:String
)