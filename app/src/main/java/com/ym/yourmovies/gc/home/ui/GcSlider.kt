package com.ym.yourmovies.gc.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.gc.home.models.GcSliderData
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GcSlider(
    sliderDataList: List<GcSliderData>,
    onSliderClick: (GcMovie) -> Unit
) {
    val lazyListState = rememberLazyListState()
     var currentIndex by remember {
         mutableStateOf(0)
     }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            state = lazyListState  ,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
        ) {
            items(sliderDataList.size) { index ->
                val sliderData = sliderDataList[index]

                GcSliderItem(
                    sliderData = sliderData,
                    onSliderClick = { onSliderClick(
                        GcMovie(
                        title = sliderData.title,
                        date = sliderData.year,
                        thumb = sliderData.thumb,
                        url =  sliderData.url,
                        rating = ""
                    )
                    ) },
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
        }
     /*
    val widthPxToScroll = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.roundToPx().toFloat()
    }
    val currentVisible by remember {
        derivedStateOf {
           lazyListState.firstVisibleItemIndex
        }
    }

      */
    if (sliderDataList.isNotEmpty()){
        LaunchedEffect(key1 = lazyListState){
            lazyListState.animateScrollToItem(0)
            while (isActive){
                delay(2000L)
                lazyListState.animateScrollToItem(if (currentIndex==sliderDataList.size)0 else currentIndex++)
            }
        }
    }
    /*
    if (sliderDataList.isNotEmpty()){
        LaunchedEffect(key1 = sliderDataList){
            while (isActive){


//                currentIndex++
//                if (currentIndex==sliderDataList.size) currentIndex = 0
                delay(2000)
                if (currentVisible==sliderDataList.size-1){
                    lazyListState.animateScrollToItem(0)
                }else{
                    lazyListState.animateScrollBy(widthPxToScroll,
                        animationSpec = tween(durationMillis = 1500)
                    )
                }
            }

        }
    }
    */

}