package com.ym.yourmovies.cm.compose

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
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.components.MyAsyncImage
import com.ym.yourmovies.utils.components.MyRating
private val thumbModifier = Modifier.layoutId("thumb")
private val ratingModifier = Modifier.layoutId("rating")
private val titleModifier = Modifier.layoutId("title").padding(horizontal = 2.dp, vertical = 3.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun CmMovieGrid(
    modifier: Modifier = Modifier,
    movie: CmMovie,
    constraintSet: ConstraintSet,
    onItemClick: (CmMovie) -> Unit
) {
    ElevatedCard(
        onClick = { onItemClick(movie) },
        modifier = modifier
            .padding(3.dp) ,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        ) ,
        shape = RoundedCornerShape(7.dp)
    ) {
        ConstraintLayout(modifier = modifier, constraintSet = constraintSet) {
            MyAsyncImage(thumbUrl = movie.thumb, modifier = thumbModifier)
            MyRating(rating = movie.rating, modifier = ratingModifier)
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = titleModifier,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}