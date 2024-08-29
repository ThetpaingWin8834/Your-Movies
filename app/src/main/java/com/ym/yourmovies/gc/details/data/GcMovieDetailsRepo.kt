package com.ym.yourmovies.gc.details.data

import androidx.core.text.HtmlCompat
import com.ym.yourmovies.gc.details.models.GcMovieDownloadData
import com.ym.yourmovies.gc.details.ui.GcMovieDetailsData
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.utils.abstracts.DetailsRepo
import com.ym.yourmovies.utils.components.MyDetailsHeaderData
import com.ym.yourmovies.utils.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class GcMovieDetailsRepo  : DetailsRepo<GcMovie,GcMovieDetailsData>{
    override suspend fun getDatas(movie: GcMovie): Flow<Response<GcMovieDetailsData>> {
           return flow {
               Scrape {
                   val document  = Jsoup.connect(movie.url).get()
                   val headerData = getHeaderData(movie, document)
                   val bodyDataList = getBodyData(movie, document)
                   val backDropList = document.getElementsByClass("g-item").map {
                       NameAndUrlModel(
                           text = it.getElementsByTag("a").attr("href"),
                           url = it.getElementsByTag("img").attr("src")
                       )
                   }
                   val downLoadList = mutableListOf<GcMovieDownloadData>()
                   document.select("#download tr").forEachIndexed { index, element ->
                       if (index!=0){
                           downLoadList.add(GcMovieDownloadData(
                               serverIcon = element.getElementsByTag("img").attr("src"),
                               quality = element.getElementsByClass("quality").text(),
                               language = element.getElementsByTag("td")[2].text(),
                               size = element.getElementsByTag("td")[3].text(),
                               url = element.getElementsByTag("a").attr("href")
                           ))
                       }
                   }

                   val relatedList = document.select("aside article").map {
                       val title = it.getElementsByTag("img").attr("alt")
                       val thumb = it.getElementsByTag("img").attr("src")
                       val rating = it.getElementsByTag("b").text()
                       val date = it.getElementsByClass("year").text()
                       val url = it.getElementsByTag("a").attr("href")
                       GcMovie(
                           title, date, rating, thumb, url
                       )

                   }

                   emit(Response.Success(
                       data = GcMovieDetailsData(
                           headerData= headerData,
                           detailsBodyDataList =  bodyDataList,
                           backDropsList = backDropList,
                           downloadList = downLoadList,
                           relatedList = relatedList
                       )
                   ))
               }
           }
    }

    override suspend fun getHeaderData(movie: GcMovie, document: Document): MyDetailsHeaderData {
        val headerEl = document.getElementsByClass("sheader").first()!!

        val vote = document.getElementById("repimdb")
        val ratingCountWithVotes = if (vote!=null){
            if (movie.rating.contains("imdb",ignoreCase = true)){
                "${movie.rating}/10 ${vote.ownText()}"
            }else{
                "IMDB : ${movie.rating}/10 ${vote.ownText()}"
            }

        }else{
            if (movie.rating.contains("imdb",ignoreCase = true)){
                "${movie.rating}/10"

            }else{
                "IMDB : ${movie.rating}/10"

            }
        }
        return  MyDetailsHeaderData(
            title = movie.title,
            thumb = movie.thumb,
            rating = movie.rating ,
            ratingWithTotal = ratingCountWithVotes ,
             date =  headerEl.getElementsByClass("date").text(),
            fullTitle = headerEl.getElementsByClass("tagline").text() ,
            duration =  headerEl.getElementsByClass("runtime").text(),
            country = headerEl.getElementsByClass("country").text() ,
            genresList = headerEl.select(".sgeneros a").map {
                NameAndUrlModel(
                    text = it.text(),
                    url = it.attr("href")
                )
            }
        )
    }

    override suspend fun getBodyData(movie: GcMovie, document: Document): List<DetailsBodyData> {
        val infoEl = document.getElementById("info")!!
        infoEl.getElementsByTag("script").remove()
        val bodyList = mutableListOf<DetailsBodyData>()
        val htmlDescription = HtmlCompat.fromHtml(infoEl.getElementsByTag("p").toString(),HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
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

        return bodyList
    }
       /*
    suspend fun getGcDetails(movie:GcMovie) = flow<Response<GcMovieDetailsData>> {
        try {
            emit(Response.Loading())

            val document  = Jsoup.connect(movie.url).get()

            val headerEl = document.getElementsByClass("sheader").first()!!

            val description = headerEl.getElementsByClass("tagline").text()
            val date = headerEl.getElementsByClass("date").text()
            val country = headerEl.getElementsByClass("country").text()
            val duration = headerEl.getElementsByClass("runtime").text()
            val contentRating = headerEl.select("span[itemprop=contentRating]").text()
            val genreList = headerEl.select(".sgeneros a").map {
                NameAndUrlModel(
                    text = it.text(),
                    url = it.attr("href")
                )
            }

            val infoEl = document.getElementById("info")!!
            val vote = infoEl.getElementById("repimdb")
            val ratingCountWithVotes = if (vote!=null){
                "IMDB : ${movie.rating}/10 ${vote.ownText()}"
            }else{
                "IMDB : ${movie.rating}/10"
            }


            val backDropList = infoEl.getElementsByClass("g-item").map {
                NameAndUrlModel(
                    text = it.getElementsByTag("a").attr("href"),
                    url = it.getElementsByTag("img").attr("src")
                )
            }
            val downLoadList = mutableListOf<GcMovieDownloadData>()
            document.select("#download tr").forEachIndexed { index, element ->

                if (index!=0){
                    downLoadList.add(GcMovieDownloadData(
                        serverIcon = element.getElementsByTag("img").attr("src"),
                        quality = element.getElementsByClass("quality").text(),
                        language = element.getElementsByTag("td")[2].text(),
                        size = element.getElementsByTag("td")[3].text(),
                        url = element.getElementsByTag("a").attr("href")
                    ))
                }
            }

            val relatedList = document.select("aside article").map {
                it.toGcMovie()
            }

            emit(Response.Success(
                data = GcMovieDetailsData(
                    headerData = GcMovieDetailsHeaderData(
                        title = movie.title,
                        description = description,
                        thumbUrl = movie.thumb,
                        date = date,
                        country = country,
                        duration = duration,
                        rating = movie.rating,
                        ratingCountWithVotes = ratingCountWithVotes,
                        contentRating = contentRating,
                        genreList = genreList
                    )                        ,
                    detailsBodyDataList = bodyList,
                    backDropsList = backDropList,
                    downloadList = downLoadList,
                    relatedList = relatedList
                )
            ))
            


        }catch (e:Exception){
            emit(Response.Error(e))
        }
    }

        */
}