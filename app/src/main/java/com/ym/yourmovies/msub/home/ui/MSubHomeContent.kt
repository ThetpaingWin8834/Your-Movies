package com.ym.yourmovies.msub.home.ui

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ym.yourmovies.YmApp
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.msub.compose.MSubMovieGrid
import com.ym.yourmovies.msub.compose.MSubMovieLinear
import com.ym.yourmovies.msub.home.data.MSubHomeViewModel
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
fun MSubHomeContent(
    isLinear: Boolean,
    cellCount: Int,
    context: Context = LocalContext.current,
    viewmodel: MSubHomeViewModel = viewModel(),
    cateItems:(List<CategoryItem>)->Unit
) {
    val adViewSmall by rememberNativeAdsLoader(nativeId = YmApp.nativeSmallId)
    val adViewMedium by rememberNativeAdsLoader(nativeId = YmApp.nativeId)


    val state by viewmodel.state.collectAsState()
    val onMovieClick = remember<(MSubMovie) -> Unit> {
        {
            Goto.msubDetails(context, it.toBundle())
        }
    }
    val onSeeAllClick = remember<(NameAndUrlModel) -> Unit> {
        {
            Goto.msubSeeAll(context = context, queryOrUrl = it.url, title = it.text)
        }
    }
    HomeContent(
        isLinear = isLinear,
        isLoading = state.isLoading,
        error = state.error,
        topContentLinear = {

        },
        topContentGrid = {

        },
        movieListDataAndCount = state.data.movieHomeData,
        tvListDataAndCount = state.data.tvHomeData,
        key = { movie ->
            movie.hashCode()
        },
        cellCount = cellCount,
        onSeeAllClick = onSeeAllClick,
        linearMovie = { mSubMovie ->
            MSubMovieLinear(
                movie = mSubMovie,
                constraintSet = Helper.mSubMovieLinearConstraintSet(),
                onMovieClick = onMovieClick
            )
        },
        gridMovie = { mSubMovie ->
            MSubMovieGrid(
                movie = mSubMovie,
                onMovieClick = onMovieClick,
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 3.dp)

            )
        },
        onErrorClick = viewmodel::getData,
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