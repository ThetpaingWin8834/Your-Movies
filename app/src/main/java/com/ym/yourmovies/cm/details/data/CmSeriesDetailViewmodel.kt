package com.ym.yourmovies.cm.details.data

import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.abstracts.AbstractViewmodel
import com.ym.yourmovies.utils.abstracts.DetailsRepo

class CmSeriesDetailViewmodel : AbstractViewmodel<CmMovie,CmSeriesDetailsData>() {
    override val repo: DetailsRepo<CmMovie, CmSeriesDetailsData>
        get() = lazy {
            CmSeriesDetailsDataRepo()
        }.value
}
data class CmSeriesDetailsData(
    val bigThumb:String,
    val description:String,
    val htmlText:String,
    val relatedList: List<CmMovie>
) {
    companion object {
        fun emptyData(): CmSeriesDetailsData = CmSeriesDetailsData(
            bigThumb = "",
            description = "",
            htmlText = "",
            relatedList = emptyList()
        )
    }
}