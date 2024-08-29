package com.ym.yourmovies.utils.abstracts

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ym.yourmovies.download.utils.LinkGenerator
import com.ym.yourmovies.utils.models.DownloadState
import com.ym.yourmovies.utils.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class AbstractViewmodel<Movie, response> : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<Exception?>(null)
        private set

    private var _data = mutableStateOf<response?>(null)


    val data: State<response?> = _data

    abstract val repo: DetailsRepo<Movie, response>

    var downloadState = MutableStateFlow(DownloadState())
    fun resetDownloadState (){
        downloadState.value = DownloadState()
    }
     var job : Job?=null

    fun fetchDirectLinkFromLeet(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            LinkGenerator.directLinkFormLeet(originalLink = url)
                .collect { response ->
                     when (response) {
                        is Response.Error -> {
                           downloadState.update { DownloadState(error = response.exception, isLoading = false) }
                        }
                        is Response.Loading -> {
                            downloadState.update {
                                DownloadState(isLoading = true)
                            }
                        }
                        is Response.Success -> {
                            downloadState.update {
                                DownloadState(generatedLink = response.data, isLoading = false)
                            }
                        }
                    }

                }
        }

    }

    fun fetchYoteShinLinkForIntent(url: String) {
       job = viewModelScope.launch(Dispatchers.IO) {
            LinkGenerator.openYoteShinDriveAppFromLink(link = url)
                .collect { response ->
                     when (response) {
                        is Response.Error -> {
                           downloadState.update { DownloadState(error = response.exception, isLoading = false) }
                        }
                        is Response.Loading -> {
                            downloadState.update {
                                DownloadState(isLoading = true)
                            }
                        }
                        is Response.Success -> {
                            downloadState.update {
                                DownloadState(generatedLink = response.data, isLoading = false)
                            }
                        }
                    }

                }
        }

    }

    fun getData(movie: Movie) {
      job =  viewModelScope.launch(Dispatchers.IO) {
            repo.getDatas(movie).collectLatest { res ->
                when (res) {
                    is Response.Error -> {
                        isLoading = false
                        error = res.exception
                    }
                    is Response.Loading -> {
                        isLoading = true
                        if (error!=null) error = null
                    }
                    is Response.Success -> {
                        isLoading = false
                        _data.value = res.data
                    }
                }
            }
        }
    }
    fun cancelJob(){
        job?.cancel()
    }
}