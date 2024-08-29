package com.ym.yourmovies.ui.search.result

import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.msub.model.MSubMovie



data class SearchResultData(
    val cmList : List<CmMovie> = emptyList(),
    val gcList : List<GcMovie> = emptyList(),
    val msubList : List<MSubMovie> = emptyList(),
    val cmError : Exception? = null,
    val gcError : Exception? = null,
    val msubError : Exception? = null,
    val isNotFoundInCm : Boolean = false,
    val isNotFoundInGc : Boolean = false,
    val isNotFoundInMsub : Boolean = false,
    val isLoading :Boolean = false,
    val query:String=""
)