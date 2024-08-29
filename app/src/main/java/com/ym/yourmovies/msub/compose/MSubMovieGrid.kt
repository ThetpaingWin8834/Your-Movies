package com.ym.yourmovies.msub.compose

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.ui.theme.Gold
import com.ym.yourmovies.utils.components.MyAsyncImage


private val thumbModifier = Modifier
    .clip(RoundedCornerShape(5.dp))
    .fillMaxWidth()
    .aspectRatio(0.65f)

private val ratingModifier = Modifier
    .clip(RoundedCornerShape(7.dp))


private val padding3 = Modifier.padding(horizontal = 3.dp, vertical = 2.dp)

private val starVector = Icons.Rounded.Star


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MSubMovieGrid(
    modifier: Modifier = Modifier,
    movie: MSubMovie,
    onMovieClick: (MSubMovie) -> Unit
) {

    ElevatedCard(
        onClick = { onMovieClick(movie) },
        modifier = modifier ,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        )      ,
        shape = RoundedCornerShape(7.dp)
    ) {
        Box{
            MyAsyncImage(
                thumbUrl = movie.thumb,
                modifier = thumbModifier
            )
            Row(
                modifier = ratingModifier
                    .background(MaterialTheme.colorScheme.background.copy(0.5f))
                    .padding(horizontal = 4.dp)
                    .align(Alignment.BottomEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = starVector, contentDescription = null, tint = Gold)
                Text(text = movie.rating ,style = MaterialTheme.typography.bodySmall)
            }
        }
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleSmall ,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1 ,
            textAlign = TextAlign.Center,
            modifier = padding3

        )
        Text(
            text = movie.date,
            style = MaterialTheme.typography.bodySmall,
            color = LocalContentColor.current.copy(alpha = 0.7f),
            modifier = padding3
        )
    }
}