package com.ym.yourmovies.cm.home.data

import com.ym.yourmovies.cm.home.models.CmHeaderData
import com.ym.yourmovies.cm.home.models.CmHomeData
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.utils.others.toCmMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup

class HomeRepositoryImpl : HomeRepository {
    override suspend fun getData(): Flow<Response<CmHomeData>> = flow {
        emit(Response.Loading())
        try {
            val doc = Jsoup.connect(MyConst.CmHost).get()
            val headerList = mutableListOf<CmHeaderData>()
            /////Header
            for (el in doc.getElementsByClass("slider_box")) {
                val title = el.getElementsByTag("h3").text()
                val list = el.getElementsByClass("item")
                    .map {
                        it.toCmMovie()
                    }
                headerList.add(CmHeaderData(title, list))
            }
            val moviesList = mutableListOf<CmMovie>()
            val tvList = mutableListOf<CmMovie>()
            for (el in  doc.select(".items .item")){
                val url = el.getElementsByTag("a").attr("href")
                if (url.contains("/tvshows/")){
                    tvList.add(el.toCmMovie())
                }else{
                    moviesList.add(el.toCmMovie())
                }
            }
           /*
            val cates = mutableListOf<CategoriesData>()
            for (el in doc.select(".categorias li")){
                val title = el.getElementsByTag("a").text()
                val link = el.getElementsByTag("a").attr("href")
                val count = el.getElementsByTag("span").text()
                cates.add(CategoriesData(
                    title, count, link
                ))
            }

            */

            emit(Response.Success(
                CmHomeData(
                    headerList = headerList,
                    movieList = moviesList,
                    tvList = tvList
                )
            ))

        }catch (e:Exception){
            e.printStackTrace()
            emit(Response.Error(e))

        }
    }
}