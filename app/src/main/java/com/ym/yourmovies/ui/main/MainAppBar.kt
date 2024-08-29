package com.ym.yourmovies.ui.main

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.others.Goto


@ExperimentalMaterial3Api
@Composable
fun MainAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationClick: () -> Unit,
    onCateClick: () -> Unit,
    cateRotation: Float,
    context: Context = LocalContext.current,
    channelName: String ,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Column {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append("Your")
                        }
                        append(" Movies")
                    },
                    fontFamily = FontFamily(Font(R.font.rubik)),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = channelName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier.padding(start = 7.dp)
                )
            }

        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
            }
        },

        actions = {
            IconButton(onClick = onCateClick) {
                Icon(
                    painterResource(id = R.drawable.ic_cate),
                    contentDescription = null,
                    modifier = Modifier.rotate(cateRotation)
                )
            }
            IconButton(onClick = {
                Goto.goSearch(context)
            }) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
            }
            IconButton(onClick = {
                Goto.goWatchLater(context)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_watch_later),
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}