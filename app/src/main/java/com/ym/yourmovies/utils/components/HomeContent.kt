package com.ym.yourmovies.utils.components

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.models.HomeListData
import com.ym.yourmovies.utils.models.NameAndUrlModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
inline fun <T> HomeContent(
    isLinear: Boolean,
    isLoading: Boolean,
    error: Exception?,
    crossinline topContentLinear: LazyListScope.() -> Unit,
    crossinline adsLinear: LazyListScope.() -> Unit,
    crossinline bottomAdsLinear: LazyListScope.() -> Unit,
    crossinline topContentGrid: LazyGridScope.() -> Unit,
    crossinline adsGrid: LazyGridScope.() -> Unit,
    crossinline bottomAdsGrid: LazyGridScope.() -> Unit,
    movieListDataAndCount: HomeListData<T>,
    tvListDataAndCount: HomeListData<T>,
    noinline key: ((T) -> Any)?,
    cellCount: Int,
    crossinline onSeeAllClick: (NameAndUrlModel) -> Unit,
    crossinline linearMovie: @Composable (T) -> Unit,
    crossinline gridMovie: @Composable (T) -> Unit,
    context: Context = LocalContext.current,
    noinline onErrorClick: () -> Unit
) {

    if (isLinear) {
        if (movieListDataAndCount.list.isNotEmpty() || tvListDataAndCount.list.isNotEmpty()) {
            LazyColumn {
                topContentLinear()
                stickyHeader (
                    key = "movieSeeAll"
                ) {
                    MySeeAll(
                        title = stringResource(id = R.string.movies),
                        count = movieListDataAndCount.data.text,
                        onBtClick = {
                            onSeeAllClick(
                                NameAndUrlModel(
                                    url = movieListDataAndCount.data.url,
                                    text = context.getString(R.string.movies)
                                )
                            )
                        }
                    )
                }
                items(
                    items = movieListDataAndCount.list,
                    key = key
                ) { item ->
                    linearMovie(item)
                }
               adsLinear()
                stickyHeader (
                    key = "tvSeeAll"
                ) {
                    MySeeAll(
                        title = stringResource(id = R.string.tv),
                        count = tvListDataAndCount.data.text,
                        onBtClick = {
                            onSeeAllClick(
                                NameAndUrlModel(
                                    url = movieListDataAndCount.data.url,
                                    text = context.getString(R.string.tv)
                                )
                            )
                        }
                    )
                }
                items(
                    items = tvListDataAndCount.list,
                    key = key
                ) { item ->
                    linearMovie(item)
                }
                item (key = "endLinear"){
                    EndDivider()
                }
                bottomAdsLinear()
            }
        }

    } else {
        if (movieListDataAndCount.list.isNotEmpty() || tvListDataAndCount.list.isNotEmpty()) {
            LazyVerticalGrid(columns = GridCells.Fixed(cellCount)) {
                topContentGrid()
                item(
                    key = "movieseeall",
                    span = {
                        GridItemSpan(cellCount)
                    }
                ) {
                    MySeeAll(
                        title = stringResource(id = R.string.movies),
                        count = movieListDataAndCount.data.text,
                        onBtClick = {
                            onSeeAllClick(
                                NameAndUrlModel(
                                    url = movieListDataAndCount.data.url,
                                    text = context.getString(R.string.movies)
                                )
                            )
                        }
                    )
                }

                items(
                    items = movieListDataAndCount.list,
                    key = key
                ) { item ->
                    gridMovie(item)
                }
                adsGrid()
                item(
                    key = "tvseeall",
                    span = {
                        GridItemSpan(cellCount)
                    }
                ) {
                    MySeeAll(
                        title = stringResource(id = R.string.tv),
                        count = tvListDataAndCount.data.text,
                        onBtClick = {
                            onSeeAllClick(
                                NameAndUrlModel(
                                    url = tvListDataAndCount.data.url,
                                    text = context.getString(R.string.tv)
                                )
                            )
                        }
                    )

                }
                items(
                    items = tvListDataAndCount.list,
                    key = key
                ) { item ->
                    gridMovie(item)
                }
                item (key = "endGrid", span = { GridItemSpan(cellCount) }){
                    EndDivider()
                }
                bottomAdsGrid()
            }
        }

    }
    LoadingAndError(isLoading = isLoading, error = error, onErrorClick = onErrorClick)
}