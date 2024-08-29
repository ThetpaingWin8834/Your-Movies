package com.ym.yourmovies.utils.abstracts

import com.ym.yourmovies.utils.models.Response
import kotlinx.coroutines.flow.Flow

interface SearchRepo<Movie> {
    suspend fun searchMoviesByName(name:String):Flow<Response<Movie>>

    suspend fun loadMore(name: String,page:Int):List<Movie>
    fun loadMoreImpl(){

    }
}