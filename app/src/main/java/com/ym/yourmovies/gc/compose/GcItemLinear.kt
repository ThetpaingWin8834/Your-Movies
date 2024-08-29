package com.ym.yourmovies.gc.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.ui.theme.Gold
import com.ym.yourmovies.utils.components.MyAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GcItemLinear(
    modifier: Modifier = Modifier,
    movie: GcMovie,
    constraintSet: ConstraintSet,
    onGcClick: (GcMovie) -> Unit
) {
    ElevatedCard(
        onClick = { onGcClick(movie) },
        modifier = Modifier.padding(vertical = 5.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        )      ,
        shape = RoundedCornerShape(7.dp)
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp),
            constraintSet = constraintSet
        ) {
            MyAsyncImage(thumbUrl = movie.thumb, modifier = Modifier.layoutId("thumb"))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.layoutId("title")
            )
            Text(
                text = movie.date,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.layoutId("date")
            )
            Row(modifier = Modifier.layoutId("rating"), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Rounded.Star, contentDescription = "rating", tint = Gold)
                Text(
                    text = movie.rating,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }


        }
    }
}