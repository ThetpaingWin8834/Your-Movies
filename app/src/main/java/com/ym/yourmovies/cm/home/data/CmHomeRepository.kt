package com.ym.yourmovies.cm.home.data

import com.ym.yourmovies.cm.home.models.CmHeaderData
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.cm.home.viewmodels.CmViewModelHome
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.models.HomeListData
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.utils.others.toCmMovie
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup

class CmHomeRepository {
    suspend fun getCmMovies() = flow {
        try {
            emit(Response.Loading())
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
            val categoriesList = doc.select(".categorias li").map {
                CategoryItem(
                    title = it.getElementsByTag("a").text(),
                    count = it.getElementsByTag("span").text(),
                    url = it.getElementsByTag("a").attr("href")
                )
            }

            emit(
                Response.Success(
                    CmViewModelHome.CmHomeData(
                        movieHomeData = HomeListData(
                            data = NameAndUrlModel(url = MyConst.CmHost+"movies"),
                            list = moviesList
                        ),
                        tvHomeData = HomeListData(
                            data = NameAndUrlModel(url = MyConst.CmHost+"tvshows") ,
                            list = tvList
                        ),
                        headerList = headerList,
                        cateList =  categoriesList
                    )
                ))

        }catch (e:Exception){
            e.printStackTrace()
            emit(Response.Error(e))

        }
    }
}