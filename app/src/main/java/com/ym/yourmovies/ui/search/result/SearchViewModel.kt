package com.ym.yourmovies.ui.search.result

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.recent.RecentEntity
import com.ym.yourmovies.ui.main.ChannelRoute
import com.ym.yourmovies.ui.search.ui.SearchScreen
import com.ym.yourmovies.ui.settings.InAll
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.utils.models.Channel.*
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.utils.others.*
import com.ym.yourmovies.watch_later.data.WatchLaterDbProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import java.net.UnknownHostException
import kotlin.system.measureTimeMillis

enum class SearchOrRecommandIn{
    Cm,Gc,Msub,All,None
}

class SearchViewModel(
    private val app: Application,
) : AndroidViewModel(app) {
    var isFocus by mutableStateOf(true)

    var query by mutableStateOf("")
        private set

    val searchSceen = MutableStateFlow(SearchScreen.Random)
    private val searchIn by lazy {
        when (MySettingsManager.getDefaultSearchIn(app)) {
            ChannelRoute.ChannelMyanmar.name->{
                SearchOrRecommandIn.Cm
            }
            ChannelRoute.GoldChannel.name->{
                SearchOrRecommandIn.Gc
            }
            ChannelRoute.MyanmarSubtitles.name->{
                SearchOrRecommandIn.Msub
            }
            else->SearchOrRecommandIn.All
        }
    }
    private fun insertRecent(recentEntity: RecentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val isExist = recentDao.isExist(recentEntity.query)
            if (isExist==null){
                recentDao.insertRecent(recentEntity)
            }
        }
    }
    private val recentDao by lazy {
        WatchLaterDbProvider.getWatchLaterDb(app).getRecentDao()
    }

    fun onQueryChange(str: String) {
        query = str

    }
    fun getMovieQuery() = query.replace(" ", "%20")

    fun onFocusChange(state: FocusState) {
        isFocus = state.isFocused
        if (!state.isFocused) {
            if (searchSceen.value == SearchScreen.Random) {
                searchSceen.update { SearchScreen.Search }
            }
        } else {
            if (searchSceen.value == SearchScreen.Search) {
                searchSceen.update { SearchScreen.Random }
            }
        }

    }

    val cmList = MutableStateFlow(emptyList<CmMovie>())
    val gcList = MutableStateFlow(emptyList<GcMovie>())
    val msubList = MutableStateFlow(emptyList<MSubMovie>())

    val cmError = MutableStateFlow<Exception?>(null)
    val gcError = MutableStateFlow<Exception?>(null)
    val msubError = MutableStateFlow<Exception?>(null)

    val isLoading = MutableStateFlow(false)

    val error = combine(cmError,gcError,msubError){cm,gc,msub->
        val list = listOfNotNull(cm,gc,msub)
        if (list.isEmpty()){
            null
        }else{
        list.reduce { acc, exception -> acc + exception }

        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L),null)

    val isNotFound = combine(isLoading,cmList,gcList,msubList,error){ loading,cm,gc,msub,errors->
       errors!=null && !loading && cm.isEmpty() && gc.isEmpty() && msub.isEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),false)

    fun onError(){
        val channlList = mutableListOf<Channel>()
        if (cmError.value!=null)channlList.add(ChannelMyanmar)
        if (gcError.value!=null)channlList.add(GoldChannel)
        if (msubError.value!=null)channlList.add(MyanmarSubMovie)
        searchMovies(retryList = channlList)


    }
    class CombineException(cause:String):Exception(cause)
    private operator fun Exception.plus(error:Exception):CombineException{
        val cause:String = (localizedMessage ?: "Unknown error occurs") + "\n\n" + (error.localizedMessage ?: "Unknown error occurs")
        return CombineException(cause)
    }
    private suspend fun analyzeCmResult(data: Deferred<MovieResult<CmMovie>>) {
        when (val result = data.await()) {
            is MovieResult.Error -> cmError.value=result.error
            is MovieResult.Success -> cmList.value=result.list
        }
    }
    private suspend fun analyzeGcResult(data:Deferred<MovieResult<GcMovie>>) {
        when (val result = data.await()) {
            is MovieResult.Error -> gcError.value=result.error
            is MovieResult.Success -> gcList.value=result.list
        }
    } private suspend fun analyzeMsubResult(data:Deferred<MovieResult<MSubMovie>>) {
        when (val result = data.await()) {
            is MovieResult.Error -> msubError.value=result.error
            is MovieResult.Success -> msubList.value=result.list
        }
    }
    ////////////////
    private suspend fun analyzeCmResult(result: MovieResult<CmMovie>) {
        when (result) {
            is MovieResult.Error -> cmError.value=result.error
            is MovieResult.Success -> cmList.value=result.list
        }
    }
    private suspend fun analyzeGcResult(result:MovieResult<GcMovie>) {
        when (result) {
            is MovieResult.Error -> gcError.value=result.error
            is MovieResult.Success -> gcList.value=result.list
        }
    } private suspend fun analyzeMsubResult(result:MovieResult<MSubMovie>) {
        when (result) {
            is MovieResult.Error -> msubError.value=result.error
            is MovieResult.Success -> msubList.value=result.list
        }
    }

    fun searchMovies(
        retryList: List<Channel> = emptyList()
    ) {
        if (query.isEmpty()) return
        query = query.trim()
        cmError.value = null
        gcError.value = null
        msubError.value = null
        isLoading.value=true
        viewModelScope.launch(Dispatchers.IO) {
            when {
                searchIn == SearchOrRecommandIn.Cm -> {
                    analyzeCmResult(async { getCmSearchResult() })
                }
                searchIn == SearchOrRecommandIn.Gc -> {
                    analyzeGcResult(async { getGcSearchResult() })
                }
                searchIn == SearchOrRecommandIn.Msub -> {
                    analyzeMsubResult(async { getMsubSearchResult() })
                }
                retryList.isNotEmpty() -> {
                    var cmJob:Job?=null
                    var gcJob:Job?=null
                    var msubJob:Job?=null
                    for (channel in retryList) {
                        when (channel) {
                            ChannelMyanmar -> {
                                cmError.value=null
                                cmJob = launch { analyzeCmResult( getCmSearchResult() ) }

                            }
                            GoldChannel -> {
                                gcError.value=null
                                gcJob = launch { analyzeGcResult( getGcSearchResult() ) }

                            }
                            MyanmarSubMovie -> {
                                msubError.value=null
                                msubJob =  launch { analyzeMsubResult(getMsubSearchResult() ) }

                            }
                            Unknown -> Unit
                        }
                    }
                    cmJob?.join()
                    gcJob?.join()
                    msubJob?.join()
                }
                else -> {
//                    analyzeCmResult(async { getCmSearchResult() })
//                    analyzeGcResult(async { getGcSearchResult() })
//                    analyzeMsubResult(async { getMsubSearchResult() })
                   val cmJob = launch { analyzeCmResult( getCmSearchResult() ) }
                   val gcJob = launch { analyzeGcResult( getGcSearchResult() ) }
                   val msubJob =  launch { analyzeMsubResult(getMsubSearchResult() ) }
                    cmJob.join()
                    gcJob.join()
                    msubJob.join()
                    insertRecent(RecentEntity(0,query.trim()))
                }
            }

            isLoading.value = false

        }
    }

//
//    fun search() {
//        if (query.isEmpty()) return
//        cmSearchResult = emptyList()
//        gcSearchResult = emptyList()
//        msubSearchResult = emptyList()
//        query = query.trim()
//        fetchJob?.cancel()
//        insertRecent(RecentEntity(0, query.replace("%20", " ")))
//        fetchJob = viewModelScope.launch(Dispatchers.IO) {
//            try {
//                isSearching = true
//                if (searchError != null) searchError = null
//                Debug.log(searchIn.toString())
//                when (searchIn) {
//                    Channel.ChannelMyanmar -> {
//                        cmSearchResult = getCmSearchResult()
//
//                    }
//                    Channel.GoldChannel -> {
//                        gcSearchResult = getGcSearchResult()
//
//                    }
//                    Channel.MyanmarSubMovie -> {
//                        msubSearchResult = getMsubSearchResult()
//                    }
//                    Channel.Unknown -> {
//                        //  cmSearchResult = getCmSearchResult()
//                        gcSearchResult = getGcSearchResult()
//                        msubSearchResult = getMsubSearchResult()
//                    }
//                }
//
//
//                if (cmSearchResult.isEmpty() && gcSearchResult.isEmpty() && msubSearchResult.isEmpty()) {
//                    searchError = NotFoundException()
//                }
//
//            } catch (e: Exception) {
//                searchError = e
//                Debug.log(e.toString())
//            } finally {
//                isSearching = false
//            }
//        }
//    }

    sealed interface MovieResult<T> {
        data class Success<T>(val list: List<T>) : MovieResult<T>
        class Error<T>(val error: Exception) : MovieResult<T>
    }

    private fun getCmSearchResult(): MovieResult<CmMovie> {
        return try {
            val document = Jsoup.connect("${MyConst.CmHost}?s=${getMovieQuery()}").get()
            val list = document.select(".items .item").map {
                it.toCmMovie()
            }
            MovieResult.Success(list)
        } catch (e: Exception) {
           MovieResult.Error(e)
        }
    }

    private suspend fun getGcSearchResult():  MovieResult<GcMovie> {
        return try {
            val document = Jsoup.connect("${MyConst.GcHost}?s=${getMovieQuery()}").get()
            val list = document.getElementsByTag("article").map {
                it.toGcMovie()
            }
            MovieResult.Success(list)
        } catch (e: Exception) {
            MovieResult.Error(e)
        }
    }

    private suspend fun getMsubSearchResult():  MovieResult<MSubMovie>  {
        return try {
            val document = Jsoup.connect("${MyConst.MSubHost}?s=${getMovieQuery()}").get()
            val list = document.getElementsByTag("article").map {
                it.toMSubMovie()
            }
            MovieResult.Success(list)
        } catch (e: Exception) {
            MovieResult.Error(e)
        }
    }
}