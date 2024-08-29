package com.ym.yourmovies.cm.seeall.data

import androidx.lifecycle.MutableLiveData
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.abstracts.AbstractSeeAllPagingSource
import com.ym.yourmovies.utils.models.SeeAllModel
import com.ym.yourmovies.utils.others.toCmMovie
import org.jsoup.Jsoup

class CmSeeAllPagingSource (
   private val data: SeeAllModel,
   currPage: MutableLiveData<Int>
        ) : AbstractSeeAllPagingSource<CmMovie>(
       data = data,
    currPage = currPage
) {
    override suspend fun loadMovies(page: Int): List<CmMovie> {
        val url = if (data.isFromSearch){
            "${MyConst.CmHost}page/$page/?s=${data.pageAndQuery.queryOrUrl}"
        }else{
            "${data.pageAndQuery.queryOrUrl}page/$page"
        }
        val document = Jsoup.connect(url).get()
        return document.select(".items .item").map {
            it.toCmMovie()
        }
    }
}