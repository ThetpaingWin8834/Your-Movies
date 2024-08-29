package com.ym.yourmovies.cm.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ym.yourmovies.cm.home.data.CmHomeRepository
import com.ym.yourmovies.cm.home.models.CmHeaderData
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.models.HomeDataState
import com.ym.yourmovies.utils.models.HomeListData
import com.ym.yourmovies.utils.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CmViewModelHome : ViewModel() {
    val state = MutableStateFlow(HomeDataState(data = CmHomeData()))
    private val cmHomeRepository by lazy {
        CmHomeRepository()
    }
init {
    getCmMovies()
}
    fun getCmMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            cmHomeRepository.getCmMovies().collect { response ->
                when (response) {
                    is Response.Error -> {
                        state.update { it.copy(isLoading = false, error = response.exception) }
                    }
                    is Response.Loading -> {
                        state.update { it.copy(isLoading = true, error = null) }

                    }
                    is Response.Success -> {
                        state.update { it.copy(isLoading = false, data = response.data) }

                    }
                }
            }
        }
    }

    data class CmHomeData(
        val movieHomeData: HomeListData<CmMovie> = HomeListData(),
        val tvHomeData: HomeListData<CmMovie> = HomeListData(),
        val headerList: List<CmHeaderData> = emptyList(),
        val cateList : List<CategoryItem> = emptyList()
    )
}