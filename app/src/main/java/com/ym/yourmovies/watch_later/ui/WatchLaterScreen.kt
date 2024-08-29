package com.ym.yourmovies.watch_later.ui

import android.content.Context
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ym.yourmovies.R
import com.ym.yourmovies.ui.theme.Gold
import com.ym.yourmovies.utils.components.MyAsyncImage
import com.ym.yourmovies.utils.components.ads.ApplovinBanner
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.others.toBundle
import com.ym.yourmovies.utils.others.toCmMovie
import com.ym.yourmovies.utils.others.toGcMovie
import com.ym.yourmovies.utils.others.toMusbMovie
import com.ym.yourmovies.watch_later.model.WatchLaterItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchLaterScreen(
    viewModel: WatchLaterViewModel = viewModel(),
    spanCount: Int,
    backPressedDispatcher: OnBackPressedDispatcher,
    context: Context = LocalContext.current

) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val watchLaterList by viewModel.watchLaterList.collectAsState()
    val isSelectedMode by viewModel.isSelectedMode.collectAsState()
    val selectedCount by viewModel.selectedCount.collectAsState(0)
    Scaffold(
        topBar = {
            MyWatchLaterAppBar(
                onBackPressedDispatcher = backPressedDispatcher,
                count = "$selectedCount",
                scrollBehavior = scrollBehavior,
                onSelectAllClick = viewModel::onSelectAll,
                onDelete = viewModel::onDelete,
                isSelectMode = isSelectedMode
            )
        }
    ) { paddings ->
        Column(modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(paddings)) {
            WatchLaterListCompose(
                modifier = Modifier.weight(1f),
                watchLaterList = watchLaterList,
                spanCount = spanCount,
                onLongClick = {
                    viewModel.toggleSelectedMode()
                },
                onMovieClick = {
                    if (isSelectedMode) {
                        viewModel.onSelect(it)
                    } else {
                        if (it.url.startsWith(MyConst.CmHost)) {
                            Goto.cmDetails(context = context, it.toCmMovie().toBundle())
                        } else if (it.url.contains(MyConst.GcHost)) {
                            Goto.gcDetails(context = context, it.toGcMovie().toBundle())
                        } else {
                            Goto.msubDetails(context = context, it.toMusbMovie().toBundle())
                        }
                    }
                }
            )
            ApplovinBanner()
        }


    }
    BackHandler(enabled = isSelectedMode) {
         viewModel.toggleSelectedMode()
    }
    LaunchedEffect(key1 = true) {
        viewModel.getWatchLaterMovieList()
    }
}

@Composable
fun WatchLaterListCompose(
    modifier: Modifier = Modifier,
    watchLaterList: List<WatchLaterItem>,
    spanCount: Int,
    onLongClick: (WatchLaterItem) -> Unit,
    onMovieClick: (WatchLaterItem) -> Unit
) {
    if (watchLaterList.isEmpty()) {
        Text(
            text = "Empty", modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    } else {
        LazyVerticalGrid(columns = GridCells.Fixed(spanCount), modifier = modifier) {
            items(watchLaterList) { item ->
                WatchLaterMovie(
                    movie = item, onMovieClick = onMovieClick, modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    onLongClick = onLongClick
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyWatchLaterAppBar(
    onBackPressedDispatcher: OnBackPressedDispatcher,
    count: String,
    isSelectMode: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onSelectAllClick: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackPressedDispatcher::onBackPressed) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        },
        title = {
            Text(text = stringResource(id = R.string.watch_later_list))
        },
        actions = {
            if (isSelectMode) {
                IconButton(onClick = onSelectAllClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_select_all),
                        contentDescription = null
                    )
                }
                Text(text = count, modifier = Modifier.padding(horizontal = 3.dp))
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                }
            }

        },
        colors = if (isSelectMode) {
            TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}

private val thumbModifier = Modifier
    .clip(RoundedCornerShape(5.dp))
    .fillMaxWidth()
    .aspectRatio(0.65f)
private val ratingModifier = Modifier
    .clip(RoundedCornerShape(7.dp))


private val padding3 = Modifier.padding(horizontal = 3.dp, vertical = 2.dp)

private val starVector = Icons.Rounded.Star

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchLaterMovie(
    modifier: Modifier = Modifier,
    movie: WatchLaterItem,
    onMovieClick: (WatchLaterItem) -> Unit,
    onLongClick: (WatchLaterItem) -> Unit

) {
    ElevatedCard(
        modifier = modifier
            .combinedClickable(
                onLongClick = {
                    onLongClick(movie)
                },
                onClick = {
                    onMovieClick(movie)
                }
            ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        ),
        shape = RoundedCornerShape(7.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (movie.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
    ) {
        Box {
            MyAsyncImage(
                thumbUrl = movie.thumb,
                modifier = thumbModifier
            )
            if (movie.isSelected){
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(7.dp)
                )
            }


            Row(
                modifier = ratingModifier
                    .background(MaterialTheme.colorScheme.background.copy(0.5f))
                    .padding(horizontal = 4.dp)
                    .align(Alignment.BottomEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = starVector, contentDescription = null, tint = Gold)
                Text(text = movie.rating, style = MaterialTheme.typography.bodySmall)
            }
        }

        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = padding3,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            text = movie.date,
            style = MaterialTheme.typography.bodySmall,
            color = LocalContentColor.current.copy(alpha = 0.7f),
            modifier = padding3
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchLaterAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: WatchLaterViewModel,
    onSearch: () -> Unit,
    onBack: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    TopAppBar(
        title = {

        },
        scrollBehavior = scrollBehavior,
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                }
                BasicTextField(
                    value = viewModel.query,
                    onValueChange = viewModel::onQueryChange,
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f),
                    singleLine = true,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(LocalContentColor.current),
                    textStyle = TextStyle.Default.copy(
                        color = LocalContentColor.current
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        onSearch()
                    })
                ) {
                    TextFieldDefaults.TextFieldDecorationBox(
                        value = viewModel.query,
                        innerTextField = it,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        placeholder = { Text(text = stringResource(R.string.search_hint)) },
                        interactionSource = interactionSource,
                        leadingIcon = {
                            IconButton(onClick = onSearch) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "Search"
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.onQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                            top = 0.dp,
                            bottom = 0.dp
                        ),

                        )
                }
                TextButton(onClick = onSearch) {
                    Text(text = stringResource(R.string.search))
                }
            }


        }
    )
}