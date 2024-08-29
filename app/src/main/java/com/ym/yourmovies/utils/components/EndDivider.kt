package com.ym.yourmovies.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.R

@Composable
fun EndDivider(
    endText: String = stringResource(id = R.string.end_reach),
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(modifier = Modifier
            .height(DividerDefaults.Thickness)
            .weight(1f)
            .background(DividerDefaults.color)
        )
        Text(text = endText, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic)
        Box(modifier = Modifier
            .height(DividerDefaults.Thickness)
            .weight(1f)
            .background(DividerDefaults.color)
        )
    }
}