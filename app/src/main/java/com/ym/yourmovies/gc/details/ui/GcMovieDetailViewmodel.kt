package com.ym.yourmovies.gc.details.ui

import androidx.lifecycle.viewModelScope
import com.ym.yourmovies.download.utils.LinkGenerator
import com.ym.yourmovies.gc.details.data.GcMovieDetailsRepo
import com.ym.yourmovies.gc.details.models.GcMovieDownloadData
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.utils.abstracts.AbstractViewmodel
import com.ym.yourmovies.utils.abstracts.DetailsRepo
import com.ym.yourmovies.utils.components.MyDetailsHeaderData
import com.ym.yourmovies.utils.models.DetailsBodyData
import com.ym.yourmovies.utils.models.DownloadState
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GcMovieDetailViewmodel : AbstractViewmodel<GcMovie,GcMovieDetailsData>() {
    override val repo: DetailsRepo<GcMovie, GcMovieDetailsData>
        get() = lazy {
            GcMovieDetailsRepo()
        }.value

    val redirectState = MutableStateFlow(DownloadState())
    fun resetRedirectState(){
        redirectState.value = DownloadState()
    }
    fun getRedirectedLink(orginal:String){
     job = viewModelScope.launch(Dispatchers.IO){
             LinkGenerator.getRedirectLinkFrom(url = orginal)
                 .collect { response ->
                     when (response) {
                         is Response.Error -> {
                             redirectState.update { DownloadState(isLoading = false,error = response.exception) }
                         }
                         is Response.Loading -> {
                             redirectState.update {
                                 DownloadState(isLoading = true)
                             }
                         }
                         is Response.Success -> {
                             redirectState.update {
                                 DownloadState(isLoading = false,generatedLink = response.data)
                             }
                         }
                     }

                 }
        }
    }
}

data class GcMovieDetailsData(
    val headerData: MyDetailsHeaderData,
    val detailsBodyDataList: List<DetailsBodyData>,
    val backDropsList : List<NameAndUrlModel>,
    val downloadList: List<GcMovieDownloadData>,
    val relatedList : List<GcMovie>
)