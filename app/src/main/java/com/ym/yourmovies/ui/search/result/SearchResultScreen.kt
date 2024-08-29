package com.ym.yourmovies.ui.search.result

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.R
import com.ym.yourmovies.cm.compose.CmMovieGrid
import com.ym.yourmovies.gc.compose.GcMovieGridConstraint
import com.ym.yourmovies.msub.compose.MSubMovieGrid
import com.ym.yourmovies.utils.components.MyDivider
import com.ym.yourmovies.utils.custom.MyError
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.Helper
import com.ym.yourmovies.utils.others.toBundle

@Composable
fun SearchResultScreen(
    context: Context = LocalContext.current,
    viewModel: SearchViewModel,
    cellCount: Int,
) {
    val cmList by viewModel.cmList.collectAsState()
    val gcList by viewModel.gcList.collectAsState()
    val msubList by viewModel.msubList.collectAsState()

    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isNotFound by viewModel.isNotFound.collectAsState()
    LazyVerticalGrid(columns = GridCells.Fixed(cellCount), modifier = Modifier.fillMaxSize()) {


        if (cmList.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SearchResultTitle(
                    title = stringResource(
                        id = R.string.result_from_cm,
                        viewModel.query.trim()
                    )
                )
            }
            items(cmList) { cmMovie ->
                CmMovieGrid(
                    movie = cmMovie,
                    constraintSet = Helper.cmMovieGridConstraint(),
                    onItemClick = {
                        Goto.cmDetails(context, it.toBundle())
                    })
            }
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                SearchShowMoreButton(onBtClick = {
                    val query = viewModel.query.trim()
                    Goto.cmSeeAll(
                        context = context,
                        queryOrUrl = query,
                        title = query,
                        isFromSearch = true
                    )
                })
            }
        }
        if (gcList.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SearchResultTitle(
                    title = stringResource(
                        id = R.string.result_from_gc,
                        viewModel.query.trim()
                    )
                )
            }
            items(gcList) { gcMovie ->
                GcMovieGridConstraint(
                    movie = gcMovie,
                    constraintSet = Helper.gcMovieGridConstraintSet(),
                    onGcClick = {
                        Goto.gcDetails(context, it.toBundle())

                    })
            }
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                SearchShowMoreButton(onBtClick = {
                    val query = viewModel.query.trim()
                    Goto.gcSeeAll(
                        context = context,
                        queryOrUrl = query,
                        title = query,
                        isFromSearch = true
                    )
                })
            }
        }
        if (msubList.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SearchResultTitle(
                    title = stringResource(
                        id = R.string.result_from_msub,
                        viewModel.query.trim()
                    )
                )
            }
            items(msubList) { msubMovie ->
                MSubMovieGrid(
                    movie = msubMovie,
                    onMovieClick = {
                        Goto.msubDetails(context, it.toBundle())
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .padding(3.dp)
                )

            }
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                SearchShowMoreButton(onBtClick = {
                    val query = viewModel.query.trim()
                    Goto.msubSeeAll(
                        context = context,
                        queryOrUrl = query,
                        title = query,
                        isFromSearch = true
                    )
                })
            }
        }
        if (isLoading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                CircularProgressIndicator(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth())
            }
        } else if (error != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                MyError(exception = error!!, onBtClick = viewModel::onError)
            }
        } else if (isNotFound) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red), contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.not_found))
                }
            }
        }


    }

}

@Composable
private fun SearchResultTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(7.dp)
    )
}

@Composable
private fun SearchShowMoreButton(
    onBtClick: () -> Unit
) {
    Column {
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBtClick,
            shape = RoundedCornerShape(7.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 5.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp
            )
        ) {
            Text(text = stringResource(id = R.string.show_more))
        }
        Spacer(modifier = Modifier.height(7.dp))
        MyDivider()
        Spacer(modifier = Modifier.height(7.dp))
    }

}