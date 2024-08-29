package com.ym.yourmovies.msub.home.data

import androidx.core.text.HtmlCompat
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.models.HomeListData
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.utils.others.toMSubMovie
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MSubHomeDataRepo {
    suspend fun getMSubMovies() = flow {
        emit(Response.Loading())
        try {
            val document = Jsoup.connect(MyConst.MSubHost).get()
            val countAndUrlelList = document.select("header>span")
            val movieCountAndUrl :NameAndUrlModel
            val tvCountAndUrl :NameAndUrlModel
            if (countAndUrlelList.size>1){
                movieCountAndUrl  = NameAndUrlModel(
                    text = countAndUrlelList[0].ownText(),
                    url = countAndUrlelList[0].getElementsByTag("a").attr("href")
                )
                tvCountAndUrl  = NameAndUrlModel(
                    text = countAndUrlelList[1].ownText(),
                    url = countAndUrlelList[1].getElementsByTag("a").attr("href")
                )
            }else{
                movieCountAndUrl  = NameAndUrlModel(
                    text = "99+",
                    url = MyConst.MSubHost
                )
                tvCountAndUrl  = NameAndUrlModel(
                    text = "99+",
                    url = MyConst.MSubHost
                )
            }
          val cateList = document.select(".genres li").map {
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
                 data = MSubHomeViewModel.MSubHomeData(
                     movieHomeData = HomeListData(
                         data = movieCountAndUrl,
                         list = getMovieList("movies", document)
                     ),
                     tvHomeData = HomeListData(
                         data = tvCountAndUrl,
                         list = getMovieList("tvshows", document)
                     ),
                     cateList = cateList
                 )
             ))


        }catch (e:Exception){
              println(e.toString())
            emit(Response.Error(e))
        }
    }
    private fun getMovieList(className :String, document: Document ):List<MSubMovie>{
        val list = mutableListOf<MSubMovie>()
        for (movies in document.getElementsByClass(className)){
            list.add(movies.toMSubMovie())
        }
        return list
    }
}