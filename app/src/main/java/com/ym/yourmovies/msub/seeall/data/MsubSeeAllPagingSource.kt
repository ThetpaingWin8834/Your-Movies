package com.ym.yourmovies.msub.seeall.data

import androidx.lifecycle.MutableLiveData
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.abstracts.AbstractSeeAllPagingSource
import com.ym.yourmovies.utils.models.SeeAllModel
import com.ym.yourmovies.utils.others.toMSubMovie
import org.jsoup.Jsoup

class MsubSeeAllPagingSource(
    private val data: SeeAllModel,
    currPage: MutableLiveData<Int>
) : AbstractSeeAllPagingSource<MSubMovie>(
    data = data,
    currPage = currPage
) {
    override suspend fun loadMovies(page: Int): List<MSubMovie> {
        return if (data.isFromSearch){
            val url =  "${MyConst.MSubHost}page/$page/?s=${data.pageAndQuery.queryOrUrl}"
            val document =Jsoup.connect(url).get()
            document.getElementsByTag("article").map {
                it.toMSubMovie()
            }
        } else{
            val url ="${data.pageAndQuery.queryOrUrl}page/$page/"
            val document =Jsoup.connect(url).get()
            document.select(".items .item").map {
                it.toMSubMovie()
            }
        }

    }
}