package com.ym.yourmovies.msub.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.models.HomeDataState
import com.ym.yourmovies.utils.models.HomeListData
import com.ym.yourmovies.utils.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MSubHomeViewModel : ViewModel() {
    val state = MutableStateFlow(HomeDataState(data =MSubHomeData()))

    private val msubHomeRepo by lazy {
        MSubHomeDataRepo()
    }
    init {
        getData()
    }
    fun getData() {
           viewModelScope.launch(Dispatchers.IO){
               msubHomeRepo.getMSubMovies()
                   .collect{ response ->
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

    data class MSubHomeData(
        val movieHomeData: HomeListData<MSubMovie>  = HomeListData(),
        val tvHomeData: HomeListData<MSubMovie> = HomeListData(),
        val cateList : List<CategoryItem> = emptyList()
    )
}