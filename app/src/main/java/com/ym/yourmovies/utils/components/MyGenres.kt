package com.ym.yourmovies.utils.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.ym.yourmovies.utils.models.NameAndUrlModel

@Composable
 fun MyGenres(
    genres: List<NameAndUrlModel>,
     onGenreClick : (NameAndUrlModel)->Unit
) {
    FlowRow(
        mainAxisSpacing = 3.dp,
        crossAxisSpacing = 3.dp,
    ) {
        for (genre in genres) {
            Text(text = genre.text,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .border(
                        0.5.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                    .clickable {
                        if (genre.url.isNotBlank()) {
                            onGenreClick(genre)
                        }

                    })
        }
    }
}