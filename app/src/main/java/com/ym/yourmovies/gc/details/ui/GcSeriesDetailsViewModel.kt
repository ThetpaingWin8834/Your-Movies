package com.ym.yourmovies.gc.details.ui

import com.ym.yourmovies.gc.details.data.GcSeriesDetailsRepo
import com.ym.yourmovies.gc.details.models.GcMovieDownloadData
import com.ym.yourmovies.gc.details.models.GcSeriesDownloadData
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.utils.abstracts.AbstractViewmodel
import com.ym.yourmovies.utils.abstracts.DetailsRepo
import com.ym.yourmovies.utils.components.MyDetailsHeaderData
import com.ym.yourmovies.utils.models.DetailsBodyData
import com.ym.yourmovies.utils.models.DownloadState
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.models.TitleAndTextModel
import kotlinx.coroutines.flow.MutableStateFlow

class GcSeriesDetailsViewModel : AbstractViewmodel<GcMovie,GcSeriesDetailsData>() {

    override val repo: DetailsRepo<GcMovie, GcSeriesDetailsData>
        get() = lazy {
            GcSeriesDetailsRepo()
        }.value

    val redirectState = MutableStateFlow(DownloadState())

    val downloadLinksState = MutableStateFlow(GcSeriesDownloadLinksState())
}
 data class GcSeriesDownloadLinksState(
     val isLoading : Boolean = true,
     val error:Exception? =null,
     val links : List<GcMovieDownloadData> = emptyList()
 )
data class GcSeriesDetailsData(
    val headerData: MyDetailsHeaderData,
    val moreData : List<TitleAndTextModel>,
    val bodyList: List<DetailsBodyData>,
    val downloadDataList : List<GcSeriesDownloadData> ,
    val backdropList :List<NameAndUrlModel>,
    val relatedList : List<GcMovie>
)