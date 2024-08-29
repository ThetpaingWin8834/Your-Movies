package com.ym.yourmovies.cm.home.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.cm.home.models.CmHeaderData
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.cm.compose.CmMovieGrid
import com.ym.yourmovies.utils.others.Helper
import com.ym.yourmovies.utils.components.MyDivider
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CmHeader(
    headerList: List<CmHeaderData>,
    onMovieClick: (CmMovie) -> Unit
) {
    if (headerList.isNotEmpty()) {
        var currentHeaderIndex by remember {
            mutableStateOf(if (headerList[0].movieList.isNotEmpty()) 0 else 1)
        }
        val lazyListState = rememberLazyListState()
        val widthPxToScroll = with(LocalDensity.current) {
            LocalConfiguration.current.screenWidthDp.dp.roundToPx() / 10f
        }
        val constraintSet = Helper.cmMovieGridConstraint()
        Column(modifier = Modifier.fillMaxWidth().padding(7.dp)) {
            if (headerList.size == 1) {
                Text(text = headerList[0].title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    items(headerList.size){ index ->
                        val cmHeader = headerList[index]
                        if (cmHeader.movieList.isNotEmpty()) {
                            ElevatedFilterChip(
                                selected = currentHeaderIndex == index,
                                onClick = { currentHeaderIndex = index },
                                elevation = FilterChipDefaults.elevatedFilterChipElevation(
                                    defaultElevation = 5.dp,
                                    pressedElevation = 0.dp,
                                    focusedElevation = 0.dp
                                ),
                                label = {
                                    Text(
                                        text = cmHeader.title,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                            )
                        }
                    }
                }

            }

            LazyRow(
                state = lazyListState,
                contentPadding = PaddingValues(5.dp)
            ) {
                items(headerList[currentHeaderIndex].movieList) { movie ->
                    CmMovieGrid(
                        modifier = Modifier.fillParentMaxWidth(0.3f),
                        movie = movie,
                        onItemClick = onMovieClick,
                        constraintSet = constraintSet
                    )
                }
            }
/*
Row(
    modifier = Modifier
        .fillMaxWidth()
        .clickable {
            isDropDownVisible = !isDropDownVisible
        }
        .padding(10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    Text(text = headerList[currentHeaderIndex].title, style = MaterialTheme.typography.titleMedium)
    Icon(
        painter = painterResource(id = R.drawable.ic_arrow_down) ,
        contentDescription = null,
        modifier = Modifier.rotate( if (isDropDownVisible) 180f else 0f)
    )
}
if (headerList.size>1){
    DropdownMenu(expanded = isDropDownVisible, onDismissRequest = {
        isDropDownVisible = !isDropDownVisible

    },
        modifier = Modifier. fillMaxWidth(0.75f)) {
        headerst.forEachIndexed { index, headerItem ->
            DropdownMenuItem(text = {
                Text(text = headerItem.title, style = MaterialTheme.typography.bodyMedium, modifier = Modifier
                    .fillMaxWidth())
            }, onClick = {
                isDropDownVisible=false
                currentHeaderIndex = index
            },
            modifier = Modifier. fillMaxWidth())
        }
    }
}
*/
            Spacer(modifier = Modifier.height(5.dp))
            MyDivider()

            LaunchedEffect(key1 = lazyListState) {
                lazyListState.animateScrollToItem(0)
                while (this.isActive) {
                    lazyListState.animateScrollBy(
                        value = widthPxToScroll, animationSpec = tween(
                            durationMillis = 2000
                        )
                    )
                    delay(2000)
                }
            }
        }
    }

}