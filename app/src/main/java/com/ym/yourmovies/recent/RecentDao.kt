package com.ym.yourmovies.recent

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentDao {

    @Query("select * from ym_recent order by id desc")
    fun getAllRecent():Flow<List<RecentEntity>>

    @Query("delete from ym_recent")
   suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertRecent(recentEntity: RecentEntity)

   @Delete
   suspend fun clearRecent(recentEntity: RecentEntity)

   @Query("select * from ym_recent where query=:query")
   fun isExist(query:String):RecentEntity?
}