package com.ym.yourmovies.utils.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import coil.compose.AsyncImage
import com.ym.yourmovies.R
import com.ym.yourmovies.ui.theme.ShimmerColor

@Composable
fun MyAsyncImage(
    modifier: Modifier = Modifier ,
    thumbUrl : String
) {
    AsyncImage(
        model = thumbUrl,
        contentDescription = "thumbnail",
        placeholder = ColorPainter(ShimmerColor),
        modifier = modifier
            .clip(RoundedCornerShape(5.dp)),
        contentScale = ContentScale.Crop ,

    )
}