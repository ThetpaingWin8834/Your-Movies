package com.ym.yourmovies.gc.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.gc.home.models.GcSliderData
import com.ym.yourmovies.utils.components.MyAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GcSliderItem(
    sliderData: GcSliderData,
    modifier: Modifier = Modifier,
    onSliderClick: () -> Unit
) {

    ElevatedCard(
        onClick = onSliderClick, modifier = modifier
            .aspectRatio(1.8f),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        ),
        shape = RoundedCornerShape(7.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            MyAsyncImage(thumbUrl = sliderData.thumb, modifier = Modifier.fillMaxSize())
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text(text = sliderData.title, style = MaterialTheme.typography.titleLarge)
                Text(text = sliderData.year, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = sliderData.tag,
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .align(Alignment.BottomEnd),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    }

}