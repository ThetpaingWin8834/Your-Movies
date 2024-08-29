package com.ym.yourmovies.cm.details.data

import androidx.core.text.HtmlCompat
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.abstracts.DetailsRepo
import com.ym.yourmovies.utils.components.MyDetailsHeaderData
import com.ym.yourmovies.utils.models.DetailsBodyData
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.utils.others.toCmMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CmSeriesDetailsDataRepo : DetailsRepo<CmMovie,CmSeriesDetailsData> {
    override suspend fun getDatas(movie: CmMovie): Flow<Response<CmSeriesDetailsData>> {
        return flow {
            emit(Response.Loading())

            try {
                val doc= Jsoup.connect(movie.url).get()
                val coverTitle = doc.getElementsByClass("cover").toString()

                val html = doc.getElementById("info")!!
                val rem1 = html.select(".myResponsiveAd, .code-block, .metadatac").remove()
//                 if (!PreferenceUtils.isCmBackdropImgShow(this@CmSeriesDetailsActivity)){
//                     val rem4=el.select(".backdrops").remove()
//                 }
                //
                //relatedlist
                val relatedList = mutableListOf<CmMovie>()
                for (el in doc.getElementsByClass("tvitemrel")) {
                    relatedList.add(el.toCmMovie())
                }
               emit(Response.Success(
                   data =  CmSeriesDetailsData(
                       bigThumb = StringUtils.substringBetween(coverTitle, "url(", ")") ,
                       description = HtmlCompat.fromHtml(coverTitle, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                       htmlText = html.toString() ,
                       relatedList = relatedList
                   )
               ))

            }catch (e:Exception){
               emit(Response.Error(e))
            }
        }
    }

    override suspend fun getHeaderData(movie: CmMovie, document: Document): MyDetailsHeaderData {
        TODO("Not yet implemented")
    }

    override suspend fun getBodyData(movie: CmMovie, document: Document): List<DetailsBodyData> {
        TODO("Not yet implemented")
    }
}