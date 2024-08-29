package com.ym.yourmovies.ui.search.random

import android.app.Application
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ym.yourmovies.recent.RecentEntity
import com.ym.yourmovies.ui.main.ChannelRoute
import com.ym.yourmovies.ui.search.result.SearchOrRecommandIn
import com.ym.yourmovies.ui.settings.InAll
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.utils.others.*
import com.ym.yourmovies.watch_later.data.WatchLaterDbProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class RandomRecomViewModel(app: Application) : AndroidViewModel(app) {

    private val recentDao by lazy {
        WatchLaterDbProvider.getWatchLaterDb(app.applicationContext).getRecentDao()
    }
    private val recommandIn by lazy {
        when (MySettingsManager.getDefaultRecomm(app)) {
            ChannelRoute.ChannelMyanmar.name->{
                SearchOrRecommandIn.Cm
            }
            ChannelRoute.GoldChannel.name->{
                SearchOrRecommandIn.Gc
            }
            ChannelRoute.MyanmarSubtitles.name->{
                SearchOrRecommandIn.Msub
            }
            InAll-> SearchOrRecommandIn.All
            else->SearchOrRecommandIn.None
        }

    }

    val randomRecommandData = MutableStateFlow(RandomRecommandData())

    private var movieChannel = Channel.Unknown

    val recentQueryList = MutableStateFlow(emptyList<RecentEntity>())

    init {
        getAllRecents()
        fetchRandomData()
    }

    fun getAllRecents() {
        viewModelScope.launch(Dispatchers.IO) {
            recentDao.getAllRecent()
                .collectLatest { recentEntities ->
                    if (recentEntities.size > 10) {
                        clearAll()
                        for (recent in recentEntities.subList(0, 4)) {
                            insertRecent(recent)
                        }
                    }
                    recentQueryList.update { recentEntities }
                }
        }
    }

    private fun insertRecent(recentEntity: RecentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            recentDao.insertRecent(recentEntity)
        }
    }

    fun clearRecent(recentEntity: RecentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            recentDao.clearRecent(recentEntity)
        }
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            recentDao.clearAll()
        }
    }

    fun fetchRandomData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val page: Int
                val host = when (recommandIn) {
                    SearchOrRecommandIn.Cm -> {
                        MyConst.CmHost
                    }
                    SearchOrRecommandIn.Gc -> {
                        MyConst.GcHost
                    }
                    SearchOrRecommandIn.Msub -> {
                        MyConst.MSubHost
                    }
                    SearchOrRecommandIn.All-> {
                        listOf(MyConst.CmHost, MyConst.GcHost, MyConst.MSubHost).random()
                    }
                    else->return@launch
                }
                randomRecommandData.update {
                    RandomRecommandData(isLoading = true)
                }

                val url: String
                //  val url = "${MyConst.CmHost}page/$page/"
                movieChannel = if (host.contains(MyConst.MSubHost)) {
                    page = (0..145).random()
                    url = "${host}movies/page/$page"
                    Channel.MyanmarSubMovie
                } else if (host.contains(MyConst.GcHost)) {
                    page = (0..110).random()

                    url = "${host}movies/page/$page"
                    Channel.GoldChannel
                } else {
                    page = (0..100).random()
                    url = "${host}page/$page"
                    Channel.ChannelMyanmar
                }
                val document = Jsoup.connect(url).get()
                val cateList: List<CategoryItem>
                val randomMovieList: List<Any>
                when (movieChannel) {
                    Channel.GoldChannel -> {
                        document.getElementById("slider-movies")?.remove()
                        document.getElementById("featured-titles")?.remove()
                        cateList = document.select(".genres li").map {
                            CategoryItem(
                                title = HtmlCompat.fromHtml(
                                    it.getElementsByTag("a").text(),
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                ).toString(),
                                count = it.getElementsByTag("i").text(),
                                url = it.getElementsByTag("a").attr("href")
                            )
                        }
                        randomMovieList = document.getElementsByTag("article").map {
                            it.toGcMovie()
                        }
                    }
                    Channel.MyanmarSubMovie -> {
                        cateList = document.select(".genres li").map {
                            CategoryItem(
                                title = HtmlCompat.fromHtml(
                                    it.getElementsByTag("a").text(),
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                ).toString(),
                                count = it.getElementsByTag("i").text(),
                                url = it.getElementsByTag("a").attr("href")
                            )
                        }
                        randomMovieList = document.select(".items .item").map {
                            it.toMSubMovie()
                        }
                    }
                    else -> {
                        cateList = document.select(".categorias li").map {
                            CategoryItem(
                                title = it.getElementsByTag("a").text(),
                                count = it.getElementsByTag("span").text(),
                                url = it.getElementsByTag("a").attr("href")
                            )
                        }
                        randomMovieList = document.select(".items .item").map {
                            it.toCmMovie()
                        }
                    }
                }
                randomRecommandData.update {
                    RandomRecommandData(
                        categoryList = cateList,
                        randomList = randomMovieList,
                        channel = movieChannel
                    )
                }
            } catch (e: Exception) {
                randomRecommandData.update {
                    RandomRecommandData(error = e, isLoading = false)
                }
                // Debug.log(e.toString())
            }

        }


    }
}