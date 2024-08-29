package com.ym.yourmovies.ui.main.maindrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.utils.components.MyDivider

@Composable
fun ChannelDesc(
    text: String,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            modifier = modifier.padding(16.dp),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        MyDivider()
        Spacer(modifier = Modifier.height(5.dp))
    }

}