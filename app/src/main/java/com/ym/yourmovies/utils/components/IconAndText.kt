package com.ym.yourmovies.utils.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
 fun IconAndText(
    id: Int,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(3.dp)
    ) {
        Icon(painter = painterResource(id = id), contentDescription = "")
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            style = style ,
            fontWeight = fontWeight
        )


    }
}