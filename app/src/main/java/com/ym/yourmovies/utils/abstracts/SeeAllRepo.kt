package com.ym.yourmovies.utils.abstracts

interface SeeAllRepo<Movie> {
    suspend fun getAllVideosByPage(link:String,page:Int):List<Movie>

}