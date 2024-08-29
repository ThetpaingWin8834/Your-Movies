package com.ym.yourmovies.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DescriptionTitle(
    title:String,
    modifier: Modifier = Modifier
) {
     Text(text = title,style = MaterialTheme.typography.labelLarge, modifier = modifier.background(MaterialTheme.colorScheme.background).padding(15.dp))
}