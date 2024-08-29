package com.ym.yourmovies.ui.main.maindrawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ym.yourmovies.R

@Composable
fun DrawerHeader(
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f), contentAlignment = Alignment.Center
    ) {
//        Column(modifier = Modifier
//            .fillMaxHeight()
//            .aspectRatio(1f), horizontalAlignment = Alignment.CenterHorizontally) {
////            Icon(
////                painter = painterResource(id = R.drawable.ic_ym_circle),
////                contentDescription = null,
////                modifier = Modifier
////                    .fillMaxWidth(0.5f)
////                    .aspectRatio(1f),
////                tint = Color.Unspecified
////            )
////            Spacer(modifier = Modifier.height(3.dp))
////            val painter =painterResource(id = R.drawable.ic_ym_rect)
////            painter.apply {
////
////            }
////            Icon(
////                painter = painter,
////                contentDescription = null,
////                modifier = Modifier
////                    .fillMaxWidth(0.5f)
////                    .aspectRatio(1f),
////                tint = Color.Unspecified
////            )
//        }
        Image(
            painter = painterResource(id = R.drawable.ic_ym_tran),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

    }
}