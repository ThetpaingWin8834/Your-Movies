package com.ym.yourmovies.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.R

@Composable
inline fun MySeeAll(
    title:String,
    count:String ="",
    crossinline onBtClick: () -> Unit
) {
     Row(
         modifier = Modifier
             .fillMaxWidth()
             .background(MaterialTheme.colorScheme.background)
             .padding(8.dp),
         verticalAlignment = Alignment.CenterVertically
         ) {
         Box(modifier = Modifier
             .clip(RoundedCornerShape(5.dp))
             .width(4.dp)
             .height(40.dp)
             .background(MaterialTheme.colorScheme.primary))
         Spacer(modifier = Modifier.width(16.dp))
           Text(text = title, style = MaterialTheme.typography.titleLarge)
         Spacer(modifier = Modifier.weight(1f))
         if (count.isNotBlank()){
             Text(
                 text = count,
                 style = MaterialTheme.typography.bodyMedium
             )
             Spacer(modifier = Modifier.width(5.dp))
         }

         MyButton(onClick = {onBtClick()}, text = stringResource(id = R.string.see_all))

     }
}