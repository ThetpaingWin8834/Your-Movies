package com.ym.yourmovies.ui.watch.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.ym.yourmovies.R
import com.ym.yourmovies.ui.watch.data.WatchServer
import com.ym.yourmovies.ui.watch.data.directLinkFromChannel
import com.ym.yourmovies.ui.watch.data.watchServerOf
import com.ym.yourmovies.ui.watch.model.WatchableItem
import com.ym.yourmovies.utils.components.MyButton
import com.ym.yourmovies.utils.components.MyDivider
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchableDia2(
    title: String,
    list: List<WatchableItem>,
    onDismiss: () -> Unit,
    channel: Channel,
    onWatch: (url: String, watchServer: WatchServer) -> Unit
) {
    var isLoading by remember {
        mutableStateOf(false)
    }
    var error by remember {
        mutableStateOf<Exception?>(null)
    }
    var linkToGenerate by remember {
        mutableStateOf(NameAndUrlModel())
    }
    var loading by remember {
        mutableStateOf("")
    }
    var forErrorReload by remember {
        mutableStateOf(false)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = true),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .animateContentSize(),
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.dismiss)
                )
            }
        },
        title = {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isLoading) {
                        stringResource(id = R.string.loading)
                    } else if (error != null) {
                        error?.localizedMessage
                            ?: stringResource(id = R.string.unknown_error)
                    } else {
                        title
                    },
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (isLoading) {
                    Text(
                        text = loading,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (error != null) {
                    MyButton(
                        onClick = { forErrorReload = !forErrorReload },
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.retry),
                        painter = painterResource(id = R.drawable.ic_retry)
                    )
                } else {
                    var currentItem by remember {
                        mutableStateOf(list[0])
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.server)} - ",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(text = currentItem.server.serverName)
                        AsyncImage(
                            model = currentItem.server.serverIcon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    MyDivider(modifier = Modifier.padding(5.dp))

                    LazyColumn {
                        items(currentItem.listOfQualityAndUrls) { item ->
                            MyButton(text ="${stringResource(id = R.string.watch)} - ${item.text}", modifier = Modifier.fillMaxWidth() ) {
                                linkToGenerate =
                                    NameAndUrlModel(text = currentItem.server.serverName, item.url)
                                isLoading = true
                            }
//                            Button(onClick = {
//                                linkToGenerate =
//                                    NameAndUrlModel(text = currentItem.server.serverName, item.url)
//                                isLoading = true
//                            }, modifier = Modifier.fillMaxWidth()) {
//                                Text(text = "${stringResource(id = R.string.watch)} - ${item.text}")
//                            }
                        }
                    }
                    if (list.size > 1) {
                        MyDivider(modifier = Modifier.padding(5.dp))
                        FlowRow(
                            mainAxisAlignment = MainAxisAlignment.Center,
                            mainAxisSpacing = 5.dp,

                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (item in list) {
                                FilterChip(
                                    selected = item.server == currentItem.server,
                                    onClick = { currentItem = item },
                                    label = {
                                        Text(
                                            text = item.server.serverName,
                                            modifier = Modifier.padding(
                                                vertical = 5.dp,
                                                horizontal = 3.dp
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }

                }
            }
        }
    )
    LaunchedEffect(key1 = isLoading) {
        while (isLoading) {
            loading = if (loading.length >= 6) {
                ""
            } else {
                "$loading ."
            }
            delay(500L)
        }
    }
    if (linkToGenerate.url.isNotEmpty()) {
        LaunchedEffect(key1 = linkToGenerate, key2 = forErrorReload) {
            withContext(Dispatchers.IO) {
                val watchServer = watchServerOf(linkToGenerate.url)
                directLinkFromChannel(channel = channel, link = linkToGenerate.url, watchServer)
                    .collect {
                        when (it) {
                            is Response.Error -> {
                                isLoading = false
                                error = it.exception
                            }
                            is Response.Loading -> {
                                if (!isLoading) isLoading = true
                            }
                            is Response.Success -> {
                                withContext(Dispatchers.Main) {
                                    onDismiss()
                                    onWatch(it.data, watchServer)
                                }
                            }
                        }
                    }
            }
        }
    }
}