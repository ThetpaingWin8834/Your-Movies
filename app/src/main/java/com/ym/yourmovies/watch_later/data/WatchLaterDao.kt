package com.ym.yourmovies.watch_later.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ym.yourmovies.watch_later.model.OrderBy
import com.ym.yourmovies.watch_later.model.WlEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchLaterDao {

    @Query("select * from ym_wl order by id desc")
   suspend fun getAllWatchLaterMovies():List<WlEntity>

    @Insert
   suspend fun insertWatchLater(wlEntity: WlEntity)

    @Delete
   suspend fun delete(wlEntity: WlEntity)


    @Query("select * from ym_wl where url=:url")
     fun getWlIfExist(url:String) :Flow<WlEntity?>



     @Query("select * from ym_wl where title like '%' || :name || '%' ")
    suspend fun searchByName(name:String):List<WlEntity>


}