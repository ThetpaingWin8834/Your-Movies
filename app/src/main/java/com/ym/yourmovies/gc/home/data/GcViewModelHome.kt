package com.ym.yourmovies.gc.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.gc.home.models.GcSliderData
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.models.HomeDataState
import com.ym.yourmovies.utils.models.HomeListData
import com.ym.yourmovies.utils.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GcViewModelHome : ViewModel() {
    val state = MutableStateFlow(HomeDataState(data = GcHomeData()))

    private val gcRepo by lazy {
        GcHomeRepository()
    }
init {
    getGcData()
}
    fun getGcData() {
        viewModelScope.launch(Dispatchers.IO) {
            gcRepo.getGcData().collect { response ->
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

    data class GcHomeData(
        val movieHomeData: HomeListData<GcMovie> = HomeListData(),
        val tvHomeData: HomeListData<GcMovie> = HomeListData(),
        val sliderList: List<GcSliderData> = emptyList(),
        val featuredList: List<GcMovie> = emptyList(),
        val cateList : List<CategoryItem> = emptyList()
    )
}