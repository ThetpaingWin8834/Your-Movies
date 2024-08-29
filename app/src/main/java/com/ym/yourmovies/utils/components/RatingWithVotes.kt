package com.ym.yourmovies.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingWithVotes(
    ratingCount: String,
    ratingWithVotes: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ratingCount,
            color = Color.DarkGray,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 7.dp, vertical = 2.dp)


        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = ratingWithVotes,
            style = MaterialTheme.typography.bodyMedium
        )

    }
}