package com.ym.yourmovies.utils.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
 fun MyElevatedButton(
    title : String,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
   onClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
         shape = shape,
        elevation =  ButtonDefaults.elevatedButtonElevation(
            defaultElevation =5.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp
        ) 
        ) {
        Text(text = title, maxLines = 1,
        overflow = TextOverflow.Ellipsis)

    }
}