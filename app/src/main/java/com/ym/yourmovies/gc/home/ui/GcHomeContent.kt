package com.ym.yourmovies.gc.home.ui

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ym.yourmovies.YmApp
import com.ym.yourmovies.gc.compose.GcItemLinear
import com.ym.yourmovies.gc.compose.GcMovieGridConstraint
import com.ym.yourmovies.gc.home.data.GcViewModelHome
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.components.HomeContent
import com.ym.yourmovies.utils.components.ads.gridNativeAdsView
import com.ym.yourmovies.utils.components.ads.linearNativeAdsView
import com.ym.yourmovies.utils.components.ads.rememberNativeAdsLoader
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.Helper
import com.ym.yourmovies.utils.others.toBundle

@Composable
fun GcHomeContent(
    isLinear: Boolean,
    cellCount: Int,
    context: Context = LocalContext.current,
    viewmodel: GcViewModelHome = viewModel(),
    cateItems: (List<CategoryItem>) -> Unit,

    ) {
    val adViewSmall by rememberNativeAdsLoader(nativeId = YmApp.nativeSmallId)
    val adViewMedium by rememberNativeAdsLoader(nativeId = YmApp.nativeId)
    val state by viewmodel.state.collectAsState()
    val onMovieClick = remember<(GcMovie) -> Unit> {
        {
            Goto.gcDetails(context, it.toBundle())
        }
    }
    val onSeeAllClick = remember<(NameAndUrlModel) -> Unit> {
        {
            Goto.gcSeeAll(context = context, queryOrUrl = it.url, title = it.text)
        }
    }
    HomeContent(
        isLinear = isLinear,
        isLoading = state.isLoading,
        error = state.error,
        topContentLinear = {
            if (state.data.sliderList.isNotEmpty()) {
                item(key = "slider") {
                    GcSlider(sliderDataList = state.data.sliderList, onSliderClick = onMovieClick)
                }
                item(key = "space") {
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }

            if (state.data.featuredList.isNotEmpty()) {
                item(key = "featuredTiles") {
                    GcFeaturedTitles(
                        movieList = state.data.featuredList,
                        onGcItemClick = onMovieClick
                    )
                }
            }
        },
        topContentGrid = {
            if (state.data.sliderList.isNotEmpty()) {
                item(
                    key = "slider",
                    span = { GridItemSpan(cellCount) }
                ) {
                    GcSlider(sliderDataList = state.data.sliderList, onSliderClick = onMovieClick)
                }
            }

            if (state.data.featuredList.isNotEmpty()) {
                item(key = "featuredTiles",
                    span = { GridItemSpan(cellCount) }
                ) {
                    GcFeaturedTitles(
                        movieList = state.data.featuredList,
                        onGcItemClick = onMovieClick
                    )
                }
            }


        },
        movieListDataAndCount = state.data.movieHomeData,
        tvListDataAndCount = state.data.tvHomeData,
        key = null,
        cellCount = cellCount,
        onSeeAllClick = onSeeAllClick,
        linearMovie = { linearMovie ->
            GcItemLinear(
                movie = linearMovie,
                constraintSet = Helper.gcMovieLinearConstraintSet(),
                onGcClick = onMovieClick
            )
        },
        gridMovie = { gridMovie ->
            GcMovieGridConstraint(
                movie = gridMovie,
                onGcClick = onMovieClick,
                constraintSet = Helper.gcMovieGridConstraintSet()
            )
        },
        onErrorClick = viewmodel::getGcData,
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
    if (!state.isLoading) {
        cateItems(state.data.cateList)
    }
}