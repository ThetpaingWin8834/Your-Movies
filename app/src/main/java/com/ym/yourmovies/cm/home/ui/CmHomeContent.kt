package com.ym.yourmovies.cm.home.ui

import android.content.Context
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ym.yourmovies.YmApp
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.cm.compose.CmMovieGrid
import com.ym.yourmovies.cm.compose.CmMovieLinear
import com.ym.yourmovies.cm.home.viewmodels.CmViewModelHome
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.others.Helper
import com.ym.yourmovies.utils.components.HomeContent
import com.ym.yourmovies.utils.components.ads.gridNativeAdsView
import com.ym.yourmovies.utils.components.ads.linearNativeAdsView
import com.ym.yourmovies.utils.components.ads.rememberNativeAdsLoader
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.toBundle

@Composable
  fun CmHomeContent(
    isLinear: Boolean,
    context:Context= LocalContext.current,
    cellCount:Int,
    viewmodel:CmViewModelHome = viewModel(),
    cateItems:(List<CategoryItem>)->Unit
) {

    val adViewSmall by rememberNativeAdsLoader(nativeId = YmApp.nativeSmallId)
    val adViewMedium by rememberNativeAdsLoader(nativeId = YmApp.nativeId)

    val state by viewmodel.state.collectAsState()
    val onMovieClick= remember <(CmMovie)->Unit>{
        {
            Goto.cmDetails(context,it.toBundle())
        }
    }
    val onSeeAllClick= remember <(NameAndUrlModel)->Unit>{
        {
            Goto.cmSeeAll(context = context, queryOrUrl = it.url, title = it.text)
        }
    }
    HomeContent(
        isLinear = isLinear ,
        isLoading = state.isLoading,
        error = state.error,
        topContentLinear ={
            item(key = "linearheader"){
                CmHeader(headerList =state.data. headerList, onMovieClick = onMovieClick)
            }
        } ,
        topContentGrid = {
           item(
               key = "gridheader",
               span = { GridItemSpan(cellCount) }
           ) {
               CmHeader(headerList = state.data. headerList, onMovieClick)

           }
        },
        movieListDataAndCount = state.data.movieHomeData ,
        tvListDataAndCount = state.data.tvHomeData,
        key = {it.hashCode()},
        cellCount = cellCount,
        onSeeAllClick = onSeeAllClick,
        linearMovie =  { movie->
              CmMovieLinear(movie = movie, constraintSet = Helper.cmMovieLinearConstraint(), onItemClick =onMovieClick)
        },
        gridMovie = { movie->
            CmMovieGrid(movie = movie, constraintSet = Helper.cmMovieGridConstraint(), onItemClick =onMovieClick)

        } ,
        onErrorClick = viewmodel::getCmMovies,
        adsLinear = {
            linearNativeAdsView(adsView = adViewSmall)
        },
        bottomAdsLinear = {
            linearNativeAdsView(adsView = adViewMedium)
        },
        bottomAdsGrid = {
            gridNativeAdsView(adsView = adViewMedium)
        },
        adsGrid = {
            gridNativeAdsView(adsView = adViewSmall)
        }
    )

    if (!state.isLoading){
        cateItems(state.data.cateList)
    }
}