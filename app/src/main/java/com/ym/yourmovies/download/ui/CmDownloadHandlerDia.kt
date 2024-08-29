package com.ym.yourmovies.download.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.ym.yourmovies.R
import com.ym.yourmovies.download.utils.LinkGenerator
import com.ym.yourmovies.ui.watch.data.WatchServer
import com.ym.yourmovies.ui.watch.data.watchServerOf
import com.ym.yourmovies.utils.components.MyAsyncImage
import com.ym.yourmovies.utils.components.MyButton
import com.ym.yourmovies.utils.models.DownloadItem
import com.ym.yourmovies.utils.others.Ym
import com.ym.yourmovies.utils.others.myCollect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class CmClickEvent {
    Download, Browser, Watch, DownWithYoteShin, Copy
//    object Download : CmClickEvent()
//    object Browser : CmClickEvent
//    object Watch : CmClickEvent
//    object DownWithYoteShin: CmClickEvent
//    object Copy : CmClickEvent
}

@Composable
fun CMdownDia(
    title: String,
    downloadItem: DownloadItem,
    context: Context = LocalContext.current,
    onDismiss: () -> Unit,
    onDownloadByDm: (String) -> Unit,
    onDownloadOrWatch: (event: CmClickEvent, url: String, watchServer: WatchServer) -> Unit,
) {
    val server = remember {
        watchServerOf(downloadItem.url)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var error by remember {
        mutableStateOf<Exception?>(null)
    }
    val scope = rememberCoroutineScope()
    var loadingText by remember {
        mutableStateOf("")
    }
//    var onErrorClick by remember {
//        mutableStateOf(false)
//    }
    var currevent by remember {
        mutableStateOf(CmClickEvent.Download)
    }
    val onClick = remember<(CmClickEvent) -> Unit> {
        { clickEvent ->
            currevent = clickEvent
            when (clickEvent) {
                CmClickEvent.Browser -> {
                    onDismiss()
                    onDownloadOrWatch(clickEvent, downloadItem.url, server)
                    //  Ym.download(context, downloadItem.url)
                }
                CmClickEvent.Copy -> {
                    onDismiss()
                    Ym.copyLink(context, downloadItem.url)
                }
                CmClickEvent.DownWithYoteShin -> {

                    scope.launch(Dispatchers.IO) {
                        LinkGenerator.openYoteShinDriveAppFromLink(link = downloadItem.url)
                            .myCollect(
                                onerror = {
                                    if (isLoading) isLoading = false
                                    error = it
                                },
                                onloading = {
                                    isLoading = true
                                    if (error != null) error = null
                                },
                                onsuccess = { intentUri ->
                                    onDismiss()
                                    onDownloadOrWatch(clickEvent, intentUri, server)
                                    //  Ym.launchYoteShinDriveApp(context, intentUri)
                                }
                            )
                    }
                }
                CmClickEvent.Download -> {
                    scope.launch(Dispatchers.IO) {
                        when (server) {
                            WatchServer.MegaUp, WatchServer.MediaFire -> {
                                LinkGenerator.directLinkFormLeet(originalLink = downloadItem.url)
                                    .myCollect(
                                        onerror = {
                                            if (isLoading) isLoading = false
                                            error = it
                                        },
                                        onloading = {
                                            isLoading = true
                                            if (error != null) error = null
                                        },
                                        onsuccess = { directLink ->

                                            onDismiss()
                                            onDownloadByDm(directLink)
                                        }
                                    )
                            }
                            WatchServer.YoteShinDrive, WatchServer.Gdrive, WatchServer.Unknown -> {
                                withContext(Dispatchers.Main) {
                                    onDismiss()
                                    onDownloadOrWatch(clickEvent, downloadItem.url, server)
                                }

                            }
                        }
                    }
                }
                CmClickEvent.Watch -> {
                    scope.launch(Dispatchers.IO) {
                        when (server) {
                            WatchServer.MegaUp, WatchServer.MediaFire -> {
                                LinkGenerator.directLinkFormLeet(downloadItem.url)
                                    .myCollect(
                                        onerror = {
                                            if (isLoading) isLoading = false
                                            error = it
                                        },
                                        onloading = {
                                            isLoading = true
                                            if (error != null) error = null
                                        },
                                        onsuccess = { directLink ->
                                            onDismiss()
                                            onDownloadOrWatch(clickEvent, directLink, server)
                                            //Goto.goWatch(context, title = title, url = directLink)
                                        }
                                    )
                            }
                            WatchServer.YoteShinDrive -> {
                                LinkGenerator.openYoteShinDriveAppFromLink(downloadItem.url)
                                    .myCollect(
                                        onerror = {
                                            if (isLoading) isLoading = false
                                            error = it
                                        },
                                        onloading = {
                                            isLoading = true
                                            if (error != null) error = null
                                        },
                                        onsuccess = { intentUri ->
                                            onDismiss()
                                            onDownloadOrWatch(clickEvent, intentUri, server)
                                            //  Ym.launchYoteShinDriveApp(context, intentUri)
                                        }
                                    )
                            }
                            WatchServer.Gdrive -> {
                                withContext(Dispatchers.Main) {
                                    onDismiss()
                                    onDownloadOrWatch(clickEvent, downloadItem.url, server)
                                }
                            }
                            WatchServer.Unknown -> Unit
                        }
                    }

                }
            }
        }
    }

    AlertDialog(
        modifier = Modifier.fillMaxWidth(0.95f),
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        ),
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dismiss))
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = loadingText,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        text = {

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (error != null) {
                    Button(
                        onClick = {
                            onClick(currevent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Text(text = stringResource(id = R.string.retry))
                    }
                } else {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(7.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MyAsyncImage(
                            thumbUrl = downloadItem.thumb,
                            modifier = Modifier
                                .fillMaxWidth(0.35f)
                                .aspectRatio(0.65f)
                        )
                        Column {
                            if (downloadItem.serverIcon.isNotEmpty()) {
                                AsyncImage(
                                    model = downloadItem.serverIcon,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clip(
                                            RoundedCornerShape(7.dp)
                                        )
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                            if (downloadItem.server.isNotEmpty()) {
                                Text(text = downloadItem.server, maxLines = 1)

                            }


                            for (text in downloadItem.more) {
                                Text(text = text, modifier = Modifier.padding(5.dp))
                            }
                        }
                    }
                    when (server) {
                        WatchServer.MegaUp -> {
                            MegaUpOrMediafireContent(onClick = onClick)
                        }
                        WatchServer.MediaFire -> {
                            MegaUpOrMediafireContent(onClick = onClick)
                        }
                        WatchServer.YoteShinDrive -> {
                            YoteShinContent(onClick = onClick)
                        }
                        WatchServer.Gdrive -> {
                            GdriveContent(onClick = onClick)

                        }
                        WatchServer.Unknown -> {
                            OtherServerContent(onClick = onClick)

                        }
                    }
                }
            }

        }
    )
    LaunchedEffect(key1 = isLoading) {
        while (isLoading) {
            loadingText = if (loadingText.length >= 6) {
                ""
            } else {
                "$loadingText ."
            }
            delay(500L)
        }
    }

}

@Composable
private fun MegaUpOrMediafireContent(
    onClick: (CmClickEvent) -> Unit,
) {
    MyButton(
        text = stringResource(id = R.string.download),
        painter = painterResource(
            id = R.drawable.ic_down,
        ),
        onClick = {
            onClick(CmClickEvent.Download)
        },
        modifier = Modifier.fillMaxWidth()
    )
    MyButton(
        text = stringResource(id = R.string.view_in_browser),
        painter = painterResource(
            id = R.drawable.ic_network,
        ),
        onClick = {
            onClick(CmClickEvent.Browser)
        },
        modifier = Modifier.fillMaxWidth()
    )
    MyButton(
        text = stringResource(id = R.string.watch),
        painter = painterResource(
            id = R.drawable.ic_eye,
        ),
        onClick = {
            onClick(CmClickEvent.Watch)

        },
        modifier = Modifier.fillMaxWidth()
    )
    MyButton(
        text = stringResource(id = R.string.copy_link),
        painter = painterResource(
            id = R.drawable.ic_copy_link,
        ),
        onClick = {
            onClick(CmClickEvent.Copy)

        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun YoteShinContent(
    onClick: (CmClickEvent) -> Unit,
) {
    MyButton(
        text = stringResource(id = R.string.download_in_yoteshin_drive),
        painter = painterResource(
            id = R.drawable.ic_down,
        ),
        onClick = {
            onClick(CmClickEvent.DownWithYoteShin)
        },
        modifier = Modifier.fillMaxWidth()
    )
    MyButton(
        text = stringResource(id = R.string.view_in_browser),
        painter = painterResource(
            id = R.drawable.ic_network,
        ),
        onClick = {
            onClick(CmClickEvent.Browser)

        },
        modifier = Modifier.fillMaxWidth()
    )
    MyButton(
        text = stringResource(id = R.string.watch),
        painter = painterResource(id = R.drawable.ic_eye),
        onClick = {
            onClick(CmClickEvent.Watch)

        },
        modifier = Modifier.fillMaxWidth()
    )
    MyButton(
        text = stringResource(id = R.string.copy_link),
        painter = painterResource(
            id = R.drawable.ic_copy_link,
        ),
        onClick = {
            onClick(CmClickEvent.Copy)
        },
        modifier = Modifier.fillMaxWidth()
    )

}


@Composable
private fun GdriveContent(
    onClick: (CmClickEvent) -> Unit,
) {
    MyButton(
        text = stringResource(id = R.string.download),
        painter = painterResource(
            id = R.drawable.ic_down,
        ),
        onClick = {
            onClick(CmClickEvent.Download)

        },
        modifier = Modifier.fillMaxWidth()
    )
    MyButton(
        text = stringResource(id = R.string.watch),
        painter = painterResource(
            id = R.drawable.ic_eye,
        ),
        onClick = {
            onClick(CmClickEvent.Watch)

        },
        modifier = Modifier.fillMaxWidth()
    )
    MyButton(
        text = stringResource(id = R.string.copy_link),
        painter = painterResource(
            id = R.drawable.ic_copy_link,
        ),
        onClick = {
            onClick(CmClickEvent.Copy)

        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
private fun OtherServerContent(
    onClick: (CmClickEvent) -> Unit,
) {
    MyButton(
        text = stringResource(id = R.string.download),
        painter = painterResource(
            id = R.drawable.ic_down,
        ),
        onClick = {
            onClick(CmClickEvent.Download)
        },
        modifier = Modifier.fillMaxWidth()
    )
    MyButton(
        text = stringResource(id = R.string.copy_link),
        painter = painterResource(
            id = R.drawable.ic_copy_link,
        ),
        onClick = {
            onClick(CmClickEvent.Copy)

        },
        modifier = Modifier.fillMaxWidth()
    )
}