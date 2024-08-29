package com.ym.yourmovies.gc.home.data

import androidx.core.text.HtmlCompat
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.gc.home.models.GcSliderData
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.models.HomeListData
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.utils.others.toGcMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup

class GcHomeRepository {
    suspend fun getGcData() : Flow<Response<GcViewModelHome.GcHomeData>> = flow {
        try {
          emit(Response.Loading())
            val doc = Jsoup.connect(MyConst.GcHost).get()
            val sliderDataList = mutableListOf<GcSliderData>()
            for (sliderEl in doc.select(".slider .item")){
                sliderDataList.add(
                    GcSliderData(
                        title = sliderEl.getElementsByTag("img").attr("alt"),
                        tag = sliderEl.getElementsByClass("item_type").text(),
                        year = sliderEl.select(".data span").text(),
                        thumb = sliderEl.getElementsByTag("img").attr("src"),
                        url = sliderEl.getElementsByTag("a").attr("href")
                    )
                )
            }

            //featuredTiles
            val featuredTitlesList = mutableListOf<GcMovie>()
            for (featuredEl in doc.select("#featured-titles article")){
                featuredTitlesList.add(featuredEl.toGcMovie())
            }
            //movies
            val moviesList = mutableListOf<GcMovie>()
            val moviesCount =  doc.select("header:contains(movies) span").text().replace("see all","",true)
            for (movieEl in doc.getElementsByClass("movies")){
                moviesList.add(movieEl.toGcMovie())
            }
            //tvShows
            val tvList = mutableListOf<GcMovie>()
            val tvCount =  doc.select("header:contains(shows) span").text().replace("see all","",true)
            for (tvEl in doc.getElementsByClass("tvshows")){
                tvList.add(tvEl.toGcMovie())
            }

           val cateList = doc.select(".genres li").map {
                CategoryItem(
                    title = HtmlCompat.fromHtml(
                        it.getElementsByTag("a").text(),
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    ).toString(),
                    count = it.getElementsByTag("i").text(),
                    url = it.getElementsByTag("a").attr("href")
                )
            }
            emit(Response.Success(
                GcViewModelHome.GcHomeData(
                    movieHomeData = HomeListData(
                        data = NameAndUrlModel(
                            text = moviesCount,
                            url = MyConst.GcHost + "movies/"
                        ),
                        list = moviesList
                    ),
                    tvHomeData = HomeListData(
                        data = NameAndUrlModel(text = tvCount, url = MyConst.GcHost + "tvshows/"),
                        list = tvList
                    ),
                    sliderList = sliderDataList,
                    featuredList = featuredTitlesList,
                    cateList =cateList
                )
            ))



        }catch (e:Exception){
            emit(Response.Error(e))
        }
    }
}