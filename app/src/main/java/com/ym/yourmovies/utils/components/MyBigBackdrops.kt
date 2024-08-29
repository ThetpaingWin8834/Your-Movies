package com.ym.yourmovies.utils.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.snapping.SnapFlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.ym.yourmovies.utils.models.NameAndUrlModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyBigBackdrops(
    backdropList: List<NameAndUrlModel>,
    onBackdropClick: (String) -> Unit
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter)
    {
        val thumbsState = rememberLazyListState() 
        var currentIndex by remember {
            mutableStateOf(0)
        }
        LazyRow(
            state =  thumbsState ,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = thumbsState)
        ){
            items(backdropList.size){ index->
                currentIndex = index
                val item = backdropList[index]
                MyAsyncImage(thumbUrl = item.text, modifier = Modifier
                    .fillParentMaxWidth()
                    .aspectRatio(2f)
                    .clickable(enabled = item.url.isNotEmpty()) { onBackdropClick(item.url) }
                )  
            }
        }
//        val firstVisibleItemIndex by remember {
//            derivedStateOf {
//                thumbsState.firstVisibleItemIndex
//            }
//        }
//        FlowRow(
//            modifier = Modifier.fillMaxWidth(),
//            mainAxisAlignment = FlowMainAxisAlignment.Center,
//            crossAxisAlignment = FlowCrossAxisAlignment.Center,
//            mainAxisSpacing = 2.dp,
//            crossAxisSpacing = 2.dp
//        ) {
//           backdropList.forEachIndexed { index, nameAndUrlModel ->
//               Box(modifier = Modifier
//                   .size(20.dp)
//                   .padding(4.dp)
//                   .background(
//                       if (firstVisibleItemIndex == index) {
//                           MaterialTheme.colorScheme.primary
//                       } else {
//                           Color.DarkGray
//                       },
//                       shape = CircleShape
//                   )
//               )
//           }
//        }
//

        LaunchedEffect(key1 = thumbsState){
            thumbsState.animateScrollToItem(0)
            while (isActive){
                delay(2000L)
                thumbsState.animateScrollToItem(if (currentIndex==backdropList.size)0 else currentIndex++)
            }
        }
    }
}