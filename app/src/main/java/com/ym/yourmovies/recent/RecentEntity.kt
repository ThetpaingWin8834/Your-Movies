package com.ym.yourmovies.recent

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ym_recent")
data class RecentEntity(
    @PrimaryKey(autoGenerate = true) val id:Int,
    val query:String
)