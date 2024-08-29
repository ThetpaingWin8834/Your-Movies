package com.ym.yourmovies.utils.abstracts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ym.yourmovies.utils.models.SeeAllModel

abstract class AbstractSeeAllViewModel<Movie : Any>(
) : ViewModel() {
    abstract val  pagingSourceFactory: () -> PagingSource<Int, Movie>
    val currPage = MutableLiveData(1)
    val pageAndQuery = MutableLiveData<SeeAllModel>()

    val pager = pageAndQuery.switchMap {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                maxSize = 100
            ),
            pagingSourceFactory = pagingSourceFactory
        ).liveData.cachedIn(viewModelScope)
    }
}