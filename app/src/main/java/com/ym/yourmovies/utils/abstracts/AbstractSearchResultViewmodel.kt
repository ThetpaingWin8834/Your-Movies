package com.ym.yourmovies.utils.abstracts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

abstract class AbstractSearchResultViewmodel :ViewModel() {
    var isLoadingMore by mutableStateOf(false)
    private set

    var isRefreshLoading by mutableStateOf(false)
        private set

    var refreshError by mutableStateOf<Exception?>(null)
        private set
    var loadMoreError by mutableStateOf<Exception?>(null)
        private set


}