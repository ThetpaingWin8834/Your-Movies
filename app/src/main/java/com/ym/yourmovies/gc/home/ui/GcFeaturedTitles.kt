package com.ym.yourmovies.gc.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.R
import com.ym.yourmovies.gc.compose.GcItemGrid

@Composable
fun GcFeaturedTitles(
    movieList: List<GcMovie>,
    onGcItemClick: (GcMovie) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .width(4.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = stringResource(id = R.string.featured_titles), style = MaterialTheme.typography.titleLarge)
        }
        LazyRow(modifier = Modifier.fillMaxWidth()){
            items(movieList){ movie->
                GcItemGrid(movie = movie, onGcClick = { onGcItemClick(movie) }, modifier = Modifier
                    .fillParentMaxWidth(0.3f)
                    .padding(3.dp))
            }
        }
    }
}