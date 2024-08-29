package com.ym.yourmovies.ui.search.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.R
import com.ym.yourmovies.cm.compose.CmMovieGrid
import com.ym.yourmovies.gc.compose.GcMovieGridConstraint
import com.ym.yourmovies.msub.compose.MSubMovieGrid
import com.ym.yourmovies.ui.search.result.SearchViewModel
import com.ym.yourmovies.utils.others.Helper
import com.ym.yourmovies.utils.components.LoadingAndError
import com.ym.yourmovies.utils.components.MyDivider
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.toBundle
/*
@Composable
fun SearchResultCompose(
    viewModel: SearchViewModel,
    spanCount: Int = 3,
    context: Context = LocalContext.current,
    focusManager: FocusManager
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(spanCount),
        modifier = Modifier
            .fillMaxSize()
            .padding(7.dp)
    ) {
        if (viewModel.cmSearchResult.isNotEmpty()) {
            item(
                span = { GridItemSpan(spanCount) }
            ) {
                SearchResultTitle(
                    title = stringResource(
                        id = R.string.result_from_cm,
                        viewModel.query
                    )
                )
            }
            items(viewModel.cmSearchResult) { movie ->
                CmMovieGrid(
                    movie = movie,
                    constraintSet = Helper.cmMovieGridConstraint(),
                    onItemClick = {
                        Goto.cmDetails(context,it.toBundle())
                    })
            }
            item(
                span = { GridItemSpan(spanCount) }
            ) {
                SearchShowMoreButton(onBtClick = {
                    Goto.cmSeeAll(
                        context = context,
                        queryOrUrl = viewModel.getMovieQuery(),
                        title = viewModel.query,
                        isFromSearch = true
                    )
                })
            }
        }
        if (viewModel.gcSearchResult.isNotEmpty()) {
            item(
                span = { GridItemSpan(spanCount) }
            ) {
                SearchResultTitle(
                    title = stringResource(
                        id = R.string.result_from_gc,
                        viewModel.query
                    )
                )

            }
            items(viewModel.gcSearchResult) { movie ->
                GcMovieGridConstraint(
                    movie = movie,
                    constraintSet = Helper.gcMovieGridConstraintSet(),
                    onGcClick = {
                        Goto.gcDetails(context,it.toBundle())

                    })

            }
            item(
                span = { GridItemSpan(spanCount) }
            ) {
                SearchShowMoreButton(onBtClick = {
                    Goto.gcSeeAll(
                        context = context,
                        queryOrUrl = viewModel.getMovieQuery(),
                        title = viewModel.query,
                        isFromSearch = true
                    )
                })
            }
        }
        if (viewModel.msubSearchResult.isNotEmpty()) {
            item(
                span = { GridItemSpan(spanCount) }
            ) {
                SearchResultTitle(
                    title = stringResource(
                        id = R.string.result_from_msub,
                        viewModel.query
                    )
                )
            }
            items(viewModel.msubSearchResult) { movie ->
                MSubMovieGrid(
                    movie = movie,
                    onMovieClick = {
                        Goto.msubDetails(context,it.toBundle())

                    },
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .padding(3.dp)
                )

            }
            item(
                span = { GridItemSpan(spanCount) }
            ) {
                SearchShowMoreButton(onBtClick = {
                    Goto.msubSeeAll(
                        context = context,
                        queryOrUrl = viewModel.getMovieQuery(),
                        title = viewModel.query,
                        isFromSearch = true
                    )
                })
            }
        }
        item(span = { GridItemSpan(spanCount) }) {
            LoadingAndError(
                isLoading = viewModel.isSearching,
                error = viewModel.searchError,
                onErrorClick = viewModel::search,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
            )
        }
    }
    LaunchedEffect(key1 = true) {
        focusManager.clearFocus()
    }
}

 */