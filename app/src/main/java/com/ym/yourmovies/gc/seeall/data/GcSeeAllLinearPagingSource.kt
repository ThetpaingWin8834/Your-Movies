package com.ym.yourmovies.gc.seeall.data

import androidx.lifecycle.MutableLiveData
import com.ym.yourmovies.cm.seeall.model.GcMovieMeta
import com.ym.yourmovies.utils.abstracts.AbstractSeeAllPagingSource
import com.ym.yourmovies.utils.models.SeeAllModel
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.others.toGcMovieMeta
import org.jsoup.Jsoup

class GcSeeAllLinearPagingSource(
    private val data: SeeAllModel,
    currPage: MutableLiveData<Int>
) : AbstractSeeAllPagingSource<GcMovieMeta>(data = data, currPage = currPage) {
    override suspend fun loadMovies(page: Int): List<GcMovieMeta> {
        val url = if (data.isFromSearch) {
            "${MyConst.GcHost}page/$page/?s=${data.pageAndQuery.queryOrUrl}"
        } else {
            "${data.pageAndQuery.queryOrUrl}page/$page/"
        }
        val document = Jsoup.connect(url).get()
        document.getElementById("slider-movies")?.remove()
        document.getElementById("featured-titles")?.remove()
        return document.getElementsByTag("article").map {
            it.toGcMovieMeta()
        }
    }
}