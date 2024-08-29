package com.ym.yourmovies.cm.details.data

import androidx.core.text.HtmlCompat
import com.ym.yourmovies.cm.details.models.CmDownloadItem
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.abstracts.DetailsRepo
import com.ym.yourmovies.utils.components.MyDetailsHeaderData
import com.ym.yourmovies.utils.models.*
import com.ym.yourmovies.utils.others.toCmMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CmDetailsDataRepo : DetailsRepo<CmMovie, CmMovieDetailsData> {


    override suspend fun getHeaderData(movie: CmMovie, document: Document): MyDetailsHeaderData {
        return MyDetailsHeaderData.emptyData()
    }

    override suspend fun getBodyData(movie: CmMovie, document: Document): List<DetailsBodyData> {
        return emptyList()
    }

    override suspend fun getDatas(movie: CmMovie): Flow<Response<CmMovieDetailsData>> {
        return flow {
            Scrape {
                val doc = Jsoup.connect(movie.url).get()

                val data = doc.getElementsByClass("data").first()!!
                val description = doc.select("meta[itemprop=description]").attr("content")
                val subTitle = data.select("i[itemprop=name]").text()
                val publishDate = data.select("i[itemprop=datePublished]").text()

                val duration = data.select("i[itemprop=duration]").text()
                val ratingWithTotal = HtmlCompat.fromHtml(
                    data.getElementsByClass("dato").text(),
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ).toString()
                val winMetaData = data.getElementsByClass("icon-trophy").parents().first()?.text()
                val country = data.getElementsByClass("icon-network").parents().first()?.text()
                val viewCount = data.getElementsByClass("icon-eye").parents().first()?.text()

                val genreList = mutableListOf<NameAndUrlModel>()
                /*
                genres
                 */
                for (el in data.select(".meta a")) {
                    genreList.add(
                        NameAndUrlModel(
                            text = el.text(),
                            url = el.attr("href")

                        )
                    )
                }
                val header = MyDetailsHeaderData(
                    title = movie.title,
                    thumb = movie.thumb,
                    fullTitle = subTitle,
                    date = publishDate,
                    duration = duration,
                    rating = movie.rating,
                    ratingWithTotal = ratingWithTotal,
                    country = country ?: "",
                    genresList = genreList
                )

                val moreData = CmMovieDetailsMoreData(
                    winMetaData = winMetaData ?: "",
                    viewCount = viewCount ?: "",
                    description = description
                )
                /*
                           trailers
                            */
                val synopsisElement = doc.getElementsByClass("entry-content").first()!!
                val metaTagsElements = synopsisElement.getElementsByClass("metatags")
                val trailersList = mutableListOf<String>()
                val trailers = synopsisElement.getElementsByClass("youtube_id")
                for (trailer in trailers) {
                    val murl = trailer.getElementsByTag("iframe").attr("src")
                    val uu = if (movie.url.startsWith("https")) {
                        murl
                    } else {
                        "https://" + murl.removeRange(0, movie.url.indexOf("www"))
                    }
                    trailersList.add(uu)
                }

                /*
                Synopsis
                 */
                val remove =
                    synopsisElement.select("style,video,script, .code-block, .generalmenu, .metatags, .youtube_id")
                        .remove()
                /*
                if (!showBackDrop) {
                    val ss = synopsisElement.select("img").remove()
                }

                 */


                val castList = mutableListOf<Casts>()
                for (el in metaTagsElements) {
                    val title = el.getElementsByTag("h3").text()
                    val list =
                        mutableListOf<CastMetaData>()
                    for (aTag in el.getElementsByTag("a")) {
                        list.add(
                            CastMetaData(
                               realname = aTag.text(),
                               url= aTag.attr("href")
                            )
                        )
                    }
                    castList.add(
                        Casts(
                            title = title,
                            castList = list
                        )
                    )
                }

                val detailsBodyDataList = mutableListOf<DetailsBodyData>()
                detailsBodyDataList.add(
                    DetailsBodyData(
                        title = "Synopsis",
                        detailsBodyModel = DetailsBodyModel.Synopsis(
                            synopsisElement.toString().trim()
                        ),

                        )
                )
                if (castList.isNotEmpty()) {
                    detailsBodyDataList.add(
                        DetailsBodyData(
                            title = "Casts",
                            detailsBodyModel = DetailsBodyModel.AllCasts(castList)
                        )
                    )
                }
                if (trailersList.isNotEmpty()) {
                    detailsBodyDataList.add(
                        DetailsBodyData(
                            title = "Trailers",
                            detailsBodyModel = DetailsBodyModel.Trailers(trailersList)
                        )
                    )
                }

                ////down
                val downList = mutableListOf<CmDownloadItem>()
                for (el in doc.select("li.elemento")) {
                    if (!el.`is`(".headers")) {
                        downList.add(
                            CmDownloadItem(
                                serverName = el.getElementsByTag("img").attr("alt"),
                                serverIcon = el.getElementsByTag("img").attr("src"),
                                size = el.select("span.c").text(),
                                quality = el.select("span.d").text(),
                                url = el.getElementsByTag("a").attr("href")
                            )
                        )
                    }

                }

                //backdrop
                val backDropList = mutableListOf<String>()
                try {
                    for (el in doc.getElementById("backdrops")!!.children()) {
                        backDropList.add(el.getElementsByTag("img").attr("src"))

                    }
                } catch (_: Exception) {
                }
                ////related
                val relatedList = mutableListOf<CmMovie>()
                for (el in doc.getElementsByClass("item")) {
                    relatedList.add(el.toCmMovie())
                }


                emit(
                    Response.Success(
                        data = CmMovieDetailsData(
                            header = header,
                            detailsBodyData = detailsBodyDataList,
                            downloadList = downList,
                            backDropList = backDropList,
                            relatedList = relatedList,
                            moreData = moreData
                        )
                    )
                )

            }
        }
    }
}