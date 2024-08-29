package com.ym.yourmovies.ui.search.random


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ym.yourmovies.R
import com.ym.yourmovies.cm.compose.CmMovieGrid
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.gc.compose.GcMovieGridConstraint
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.msub.compose.MSubMovieGrid
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.recent.RecentEntity
import com.ym.yourmovies.utils.components.MyDivider
import com.ym.yourmovies.utils.custom.MyError
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.Helper
import com.ym.yourmovies.utils.others.toBundle

@Composable
fun RandomRecommandScreen(
    cellCount: Int,
    context: Context = LocalContext.current,
    onCateMoreClick: (List<CategoryItem>) -> Unit,
    onRecentClick: (RecentEntity) -> Unit,
    viewModel: RandomRecomViewModel = viewModel(),
) {

    val state by viewModel.randomRecommandData.collectAsState()
    val recentList by viewModel.recentQueryList.collectAsState()
    LazyVerticalGrid(columns = GridCells.Fixed(cellCount), modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 7.dp),) {
        ///recent
        item(span = { GridItemSpan(maxLineSpan) }) {
            RecentCompose(
                recentList = recentList,
                onRecentClick = onRecentClick,
                onClearAll = viewModel::clearAll,
                onClear = viewModel::clearRecent
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            MyDivider()
        }
        if (state.isLoading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth())

            }
        } else if (state.error != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                MyError(
                    exception = state.error!!,
                    modifier = Modifier.fillMaxSize(),
                    onBtClick = viewModel::fetchRandomData
                )
            }

        } else {

            if (state.categoryList.isNotEmpty()) {
                val shortCateList = if (state.categoryList.size > 10) {
                    state.categoryList.subList(0, 10) + CategoryItem(
                        title = "...",
                        count = "",
                        url = ""
                    )
                } else {
                    state.categoryList
                }
                item(span = { GridItemSpan((maxLineSpan)) }) {
                    RandomRecommandHeader(
                        title = stringResource(id = R.string.categories),
                        count = state.categoryList.size
                    )
                }
                item(span = { GridItemSpan(3) }) {
                    CategoryCompose(cateList = shortCateList, onCateClick = {
                        if (it.url.isEmpty()) {
                            onCateMoreClick(state.categoryList)
                        } else {
                            Goto.onCateClick(
                                context = context,
                                movieChannels = state.channel,
                                categoryItem = it
                            )
                        }
                    })
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    MyDivider()
                }
            }
            if (state.randomList.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    RandomRecommandHeader(
                        title = stringResource(id = R.string.random_movies),
                        count = state.randomList.size
                    )
                }
                when (state.channel) {
                    Channel.ChannelMyanmar -> {
                        items(state.randomList) { movie ->
                            CmMovieGrid(movie = movie as CmMovie,
                                constraintSet = Helper.cmMovieGridConstraint(),
                                onItemClick = {
                                    Goto.cmDetails(context, it.toBundle())
                                })
                        }
                    }
                    Channel.GoldChannel -> {
                        items(state.randomList) { movie ->
                            GcMovieGridConstraint(movie = movie as GcMovie,
                                constraintSet = Helper.gcMovieGridConstraintSet(),
                                onGcClick = {
                                    Goto.gcDetails(context, it.toBundle())
                                })
                        }
                    }
                    Channel.MyanmarSubMovie -> {
                        items(state.randomList) { movie ->
                            MSubMovieGrid(movie = movie as MSubMovie,
                                modifier = Modifier.padding(horizontal = 2.dp, vertical = 3.dp),
                                onMovieClick = {
                                    Goto.msubDetails(context, it.toBundle())
                                })
                        }
                    }
                    Channel.Unknown -> Unit
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    MyDivider()
                }
            }


        }
    }
}

@Composable
private fun RandomRecommandHeader(
    title: String,
    count: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Total - $count", style = MaterialTheme.typography.bodyMedium)
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryCompose(
    cateList: List<CategoryItem>,
    onCateClick: (CategoryItem) -> Unit,
) {
    FlowRow {
        for (cate in cateList) {
            Category(cate = cate, onCateClick = onCateClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Category(
    cate: CategoryItem,
    onCateClick: (CategoryItem) -> Unit,
) {

    ElevatedCard(
        modifier = Modifier.padding(5.dp),
        shape = RoundedCornerShape(7.dp),
        onClick = { onCateClick(cate) },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        )
    ) {
        if (cate.url.isNotEmpty()) {
            Text(text = cate.title, modifier = Modifier.padding(horizontal = 3.dp, vertical = 2.dp))
            Text(
                text = cate.count,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
        } else {
            Text(
                text = cate.title,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecentCompose(
    recentList: List<RecentEntity>,
    onRecentClick: (RecentEntity) -> Unit,
    onClearAll: () -> Unit,
    onClear: (RecentEntity) -> Unit,
) {
    Column {
        if (recentList.isEmpty()) {
            Text(text = stringResource(id = R.string.recent_empty))
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.recent_search))
                TextButton(onClick = onClearAll) {
                    Text(text = stringResource(id = R.string.clear_all))
                }
            }
            MyDivider()
            Spacer(modifier = Modifier.height(3.dp))
            LazyRow(contentPadding = PaddingValues(3.dp)) {
                items(recentList) { recent ->
                    ElevatedAssistChip(onClick = { onRecentClick(recent) },
                        label = {
                            Text(text = recent.query)
                        },
                        trailingIcon = {
                            IconButton(onClick = { onClear(recent) }) {
                                Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                            }
                        },
                    modifier = Modifier.padding(3.dp))
                }
            }
        }
    }

}