package com.ym.yourmovies.cm.details.data

import com.ym.yourmovies.cm.details.models.CmDownloadItem
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.abstracts.AbstractViewmodel
import com.ym.yourmovies.utils.abstracts.DetailsRepo
import com.ym.yourmovies.utils.components.MyDetailsHeaderData
import com.ym.yourmovies.utils.models.DetailsBodyData

class CmMovieDetailViewmodel :AbstractViewmodel<CmMovie,CmMovieDetailsData>() {
    override val repo: DetailsRepo<CmMovie, CmMovieDetailsData>
        get()= lazy {
            CmDetailsDataRepo()
        }.value
}
 data class CmMovieDetailsMoreData(
     val winMetaData: String,
     val viewCount: String,
     val  description: String,
 )
data class CmMovieDetailsData (
    val header: MyDetailsHeaderData,
    val moreData: CmMovieDetailsMoreData,
    val detailsBodyData: List<DetailsBodyData>,
    val downloadList: List<CmDownloadItem>,
    val backDropList : List<String>,
    val relatedList: List<CmMovie>
)