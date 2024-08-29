package com.ym.yourmovies.msub.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.ui.theme.Gold
import com.ym.yourmovies.utils.components.MyAsyncImage

private val padding2 = Modifier.padding(vertical = 2.dp)

private val constraintModifier = Modifier.fillMaxWidth().padding(5.dp)

private val thumbModifier = Modifier.layoutId("thumb")
private val titleModifier = Modifier.layoutId("title").padding(2.dp)
private val dateModifier = Modifier.layoutId("date")
private val ratingModifier = Modifier.layoutId("rating")
private val starVector = Icons.Rounded.Star

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MSubMovieLinear(
    modifier: Modifier = padding2,
    movie: MSubMovie,
    constraintSet: ConstraintSet,
    onMovieClick: (MSubMovie) -> Unit
) {
    ElevatedCard(
        onClick = { onMovieClick(movie) },
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        ),
        shape = RoundedCornerShape(7.dp)
    ) {

        ConstraintLayout(
            modifier = constraintModifier,
            constraintSet = constraintSet
        ) {
            MyAsyncImage(thumbUrl = movie.thumb, modifier = thumbModifier)
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = titleModifier,
                textAlign = TextAlign.Center
            )
            Text(
                text = movie.date,
                style = MaterialTheme.typography.bodyMedium,
                modifier = dateModifier
            )
            Row(
                modifier = ratingModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = starVector, contentDescription = null, tint = Gold)
                Text(
                    text = movie.rating,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }


        }


    }
}