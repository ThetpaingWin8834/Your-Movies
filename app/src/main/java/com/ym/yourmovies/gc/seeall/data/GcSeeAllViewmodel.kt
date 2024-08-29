package com.ym.yourmovies.gc.seeall.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ym.yourmovies.utils.models.SeeAllModel

class GcSeeAllViewmodel :ViewModel() {
    val currPage = MutableLiveData<Int>()
    fun getPager()=  pageAndQuery.switchMap {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false,
                    maxSize = 100
                ),
                pagingSourceFactory = {
                   GcSeeAllPagingSource(it,currPage)
                }
            ).liveData.cachedIn(viewModelScope)
        }

    val pageAndQuery = MutableLiveData<SeeAllModel>()

    fun getLinearPager()=  pageAndQuery.switchMap {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false,
                    maxSize = 100
                ),
                pagingSourceFactory = {
                   GcSeeAllLinearPagingSource(it,currPage)
                }
            ).liveData.cachedIn(viewModelScope)
        }

}