package com.ym.yourmovies.cm.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.ui.theme.ShimmerColor

private val cardModifier = Modifier
    .fillMaxWidth(0.33f)
    .aspectRatio(0.65f)
    .padding(3.dp)

private val boxContentModifier = Modifier
    .clip(RoundedCornerShape(7.dp))
    .padding(horizontal = 4.dp)
@ExperimentalMaterial3Api
@Composable
fun CmMovieNoConstriant(
    item: CmMovie,
    modifier: Modifier = cardModifier,
    onclick: (item: CmMovie) -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = { onclick(item) },
        shape = RoundedCornerShape(7.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            AsyncImage(
                model = item.thumb,
                contentDescription = "Thumbnails",
                modifier = Modifier.fillMaxSize(),
                placeholder = ColorPainter(ShimmerColor),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = boxContentModifier.background(LocalContentColor.current.copy(alpha = 0.4f)).align(Alignment.BottomEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "",
                    tint = Color(0xFFFFB700)
                )
                Text(
                    text = item.rating,
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
        Text(
            text = item.title,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(3.dp),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        //  Text(text = item.date , style = MaterialTheme.typography.bodyMedium)
    }

}