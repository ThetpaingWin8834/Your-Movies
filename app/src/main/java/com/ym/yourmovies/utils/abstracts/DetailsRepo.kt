package com.ym.yourmovies.utils.abstracts

import com.ym.yourmovies.utils.components.MyDetailsHeaderData
import com.ym.yourmovies.utils.models.DetailsBodyData
import com.ym.yourmovies.utils.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import org.jsoup.nodes.Document

interface DetailsRepo<Movie,response> {
    suspend fun FlowCollector<Response<response>>.Scrape(work: suspend ()->Unit){
        try {
            emit(Response.Loading())
            work()
        }catch (e:Exception){
            emit(Response.Error(e))
        }
    }
     suspend fun getDatas(movie: Movie):Flow<Response<response>>
     suspend fun getHeaderData(movie: Movie,document: Document):MyDetailsHeaderData
     suspend fun getBodyData(movie: Movie,document: Document):List<DetailsBodyData>
}