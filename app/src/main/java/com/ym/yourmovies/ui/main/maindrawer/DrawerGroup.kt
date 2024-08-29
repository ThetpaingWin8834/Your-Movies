package com.ym.yourmovies.ui.main.maindrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerGroup(
    groupTitle: String,
    groupContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = groupTitle,
            style = MaterialTheme.typography.bodySmall
        )
        groupContent()
    }
}