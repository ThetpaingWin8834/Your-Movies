package com.ym.yourmovies.gc.details.data

import androidx.core.text.HtmlCompat
import com.ym.yourmovies.gc.details.models.GcSeriesDownloadData
import com.ym.yourmovies.gc.details.models.GcSeriesDownloadDataItem
import com.ym.yourmovies.gc.details.models.GcSeriesDownloadHeader
import com.ym.yourmovies.gc.details.ui.GcSeriesDetailsData
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.utils.abstracts.DetailsRepo
import com.ym.yourmovies.utils.components.MyDetailsHeaderData
import com.ym.yourmovies.utils.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class GcSeriesDetailsRepo : DetailsRepo<GcMovie,GcSeriesDetailsData> {


    override suspend fun getDatas(movie: GcMovie): Flow<Response<GcSeriesDetailsData>> {
       return flow {
           Scrape {
               val document = Jsoup.connect(movie.url).get()
               val headerData = getHeaderData(movie = movie, document = document)
               val bodyData = getBodyData(movie, document)
               val downloadList = document.select("#seasons .se-c").map {
                   GcSeriesDownloadData(
                       header = GcSeriesDownloadHeader(
                           seasonIndex = it.getElementsByClass("se-t").text(),
                           title = it.getElementsByClass("title").text(),
                           date = it.getElementsByTag("i").text()
                       ) ,
                       downloadList = it.getElementsByTag("li").map { dl->
                           GcSeriesDownloadDataItem(
                               thumb = dl.getElementsByTag("img").attr("src"),
                               title = dl.getElementsByClass("numerando").text(),
                               episode = dl.getElementsByTag("a").text(),
                               date = dl.getElementsByClass("date").text(),
                               url = dl.getElementsByTag("a").attr("href")
                           )
                       }
                   )
               }

               val relatedList = document.getElementsByTag("article").map {
                   GcMovie(
                       title = it.getElementsByTag("img").attr("alt"),
                       thumb = it.getElementsByTag("img").attr("src"),
                       url = it.getElementsByTag("a").attr("href"),
                       date = it.getElementsByClass("year").text(),
                       rating = it.getElementsByTag("b").text()
                   )
               }
               val backDropList = document.select("#info .g-item").map {
                   NameAndUrlModel(
                       text = it.getElementsByTag("a").attr("href"),
                       url = it.getElementsByTag("img").attr("src")
                   )
               }
               val moreData = document.select("#info .custom_fields").map {
                   TitleAndTextModel(
                       title = it.getElementsByClass("variante") .text(),
                       text = it.getElementsByClass("valor").html()
                   )
               }

               emit(Response.Success(
                   data = GcSeriesDetailsData(
                       headerData=headerData,
                       bodyList = bodyData,
                       moreData = moreData,
                       downloadDataList = downloadList,
                       relatedList = relatedList,
                       backdropList = backDropList
                   )
               ))
           }
       }
    }

    override suspend fun getHeaderData(movie: GcMovie, document: Document): MyDetailsHeaderData {
        val genreList = document.select(".sgeneros a").map {
            NameAndUrlModel(
                text = it.html(),
                url = it.attr("href")
            )
        }
        val network = document.select(".extra a[rel=tag]").let { els->
            els.map {
                NameAndUrlModel(
                    text = it.text(),
                    url = it.attr("href")
                )
            }

        }
        return MyDetailsHeaderData(
            title = movie.title,
            thumb = movie.thumb,
            rating = movie.rating,
            ratingWithTotal = "IMDB : ${movie.rating}/10",
            fullTitle = "",
            date =  document.select(".sheader .date").text(),
            duration = "",
            country = "",
            genresList = genreList +network
        )
    }

    override suspend fun getBodyData(movie: GcMovie, document: Document): List<DetailsBodyData> {
        val bodyList = mutableListOf<DetailsBodyData>()
        val infoEl = document.getElementById("info")!!
        infoEl.getElementsByTag("script").remove()
        val htmlDescription = HtmlCompat.fromHtml(
            infoEl.getElementsByTag("p").toString(),
            HtmlCompat.FROM_HTML_MODE_COMPACT
        ).toString()
        bodyList.add(
            DetailsBodyData(
                title = "Synopsis",
                detailsBodyModel = DetailsBodyModel.Synopsis(htmlString = htmlDescription)
            )
        )
        val castEl = document.getElementById("cast")
        if (castEl!=null){
            val h2Els = castEl.getElementsByTag("h2")
            val personsEls = castEl.getElementsByClass("persons")

            val castsList = personsEls.mapIndexed { index, element ->
                Casts(
                    title = h2Els[index].text(),
                    castList = element.getElementsByClass("person").map { person->
                        CastMetaData(
                            realname =  person.getElementsByTag("a").text(),
                            movieName = person.getElementsByClass("caracter").text(),
                            thumb = person.getElementsByTag("img").attr("src") ,
                            url = person.getElementsByTag("a").attr("href")
                        )
                    }
                )
            }
            bodyList.add(
                DetailsBodyData(
                    title = "Casts",
                    detailsBodyModel = DetailsBodyModel.AllCasts(castsList)
                )
            )

        }
        val trailers = document.select("#trailer iframe").map { it.attr("src") }
        if (trailers.isNotEmpty()){
            bodyList.add(
                DetailsBodyData(
                    title = "Trailers",
                    detailsBodyModel = DetailsBodyModel.Trailers(
                        trailersList = trailers
                    )
                )
            )
        }
        return bodyList
    }

}