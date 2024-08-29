package com.ym.yourmovies.cm.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.ui.theme.Gold
import com.ym.yourmovies.utils.components.MyAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun CmMovieLinear(
    modifier: Modifier = Modifier,
    movie: CmMovie,
    constraintSet: ConstraintSet,
    onItemClick: (CmMovie) -> Unit,
) {

    ElevatedCard(
        onClick = { onItemClick(movie) },
        modifier = Modifier
            .padding(5.dp)   ,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        )  ,
        shape = RoundedCornerShape(7.dp)
    ) {

        ConstraintLayout(
            constraintSet = constraintSet,
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {

            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                modifier = Modifier
                    .padding(10.dp)
                    .layoutId("title")
            )
           MyAsyncImage(thumbUrl = movie.thumb, modifier = Modifier.layoutId("thumb"))
            Text(
                text = movie.description,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp)
                    .layoutId("desc")
            )
            Row(
                modifier = Modifier
                    .layoutId("rating"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Rounded.Star, contentDescription = "Rating", tint = Gold)
                Text(
                    text = movie.rating,
                    style = MaterialTheme.typography.bodyMedium,

                    )
            }


        }
    }


}