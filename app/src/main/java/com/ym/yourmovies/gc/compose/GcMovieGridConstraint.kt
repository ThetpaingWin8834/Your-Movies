package com.ym.yourmovies.gc.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.utils.components.MyAsyncImage
import com.ym.yourmovies.utils.components.MyRating

private val thumbModifier = Modifier.layoutId("thumb")
private val ratingModifier = Modifier.layoutId("rating")
private val dateModifier = Modifier.layoutId("date")
    .padding(horizontal = 3.dp, vertical = 2.dp)
private val titleModifier = Modifier
    .layoutId("title")
    .padding(horizontal = 3.dp, vertical = 2.dp)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GcMovieGridConstraint(
    modifier: Modifier = Modifier,
    constraintSet: ConstraintSet,
    movie: GcMovie,
    onGcClick: (GcMovie) -> Unit

) {
    ElevatedCard(
        onClick = { onGcClick(movie) },
        modifier = modifier.padding(3.dp) ,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        )     ,
        shape = RoundedCornerShape(7.dp)
    ) {
        ConstraintLayout(
            constraintSet = constraintSet
        ) {
            MyAsyncImage(
                thumbUrl = movie.thumb ,
                modifier = thumbModifier
            )
            MyRating(rating = movie.rating, modifier = ratingModifier)
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = titleModifier,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Text(
                text = movie.date,
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                modifier = dateModifier
            )
        }

    }
}