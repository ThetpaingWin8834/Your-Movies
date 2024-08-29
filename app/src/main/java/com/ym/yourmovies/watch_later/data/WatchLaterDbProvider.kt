package com.ym.yourmovies.watch_later.data

import android.content.Context
import androidx.room.Room

object WatchLaterDbProvider {
    private var db :WatchLaterDb? = null
    fun getWatchLaterDb(context:Context) :WatchLaterDb {
        if (db==null){
            db = Room.databaseBuilder(context = context , klass = WatchLaterDb::class.java,"watch_later.db").build()
        }
        return db!!
    }
}