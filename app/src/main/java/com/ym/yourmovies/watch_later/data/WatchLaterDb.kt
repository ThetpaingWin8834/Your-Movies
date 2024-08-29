package com.ym.yourmovies.watch_later.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ym.yourmovies.recent.RecentDao
import com.ym.yourmovies.recent.RecentEntity
import com.ym.yourmovies.watch_later.model.WlEntity

@Database(entities = [WlEntity::class,RecentEntity::class], version = 2, exportSchema = false)
abstract class WatchLaterDb : RoomDatabase(){
    abstract fun getWlDao(): WatchLaterDao
    abstract fun getRecentDao():RecentDao
}