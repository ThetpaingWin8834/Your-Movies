package com.ym.yourmovies.download.ui

import android.content.Context
import android.text.format.Formatter
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.ym.yourmovies.R
import com.ym.yourmovies.download.utils.LinkGenerator
import com.ym.yourmovies.gc.details.models.GcSeriesDownloadDataItem
import com.ym.yourmovies.msub.model.YoutubeDownloadModel
import com.ym.yourmovies.utils.components.*
import com.ym.yourmovies.utils.models.DownloadItem
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.Ym
import com.ym.yourmovies.utils.others.myCollect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup

data class MsubDownItem(
    val isMyanmar: Boolean,
    val actionForMyanmar: String,
    val downloadItem: DownloadItem,
)

@Composable
fun MsubDownloadDia(
    downloadItem: MsubDownItem,
    ondismiss: () -> Unit,
    onDownLoad: (DownloadItem) -> Unit,
) {

    if (downloadItem.isMyanmar) {
        YtDia(
            downloadItem = downloadItem.downloadItem, onDismiss = ondismiss, onDownLoad = {
                ondismiss()
                onDownLoad(it)
            }, actionForMyanmar = downloadItem.actionForMyanmar
        )
    } else if (downloadItem.downloadItem.url.contains("myanmarsexstory.com")) {
        MsexDia(downloadItem = downloadItem.downloadItem, onDismiss = ondismiss, onDownLoad = {
            ondismiss()
            onDownLoad(it)
        })
    } else {
        ondismiss()
        onDownLoad(downloadItem.downloadItem)
    }
}

@Composable
fun MsexDia(
    downloadItem: DownloadItem,
    onDismiss: () -> Unit,
    context: Context = LocalContext.current,
    onDownLoad: (DownloadItem) -> Unit,
) {
    var isLoading by remember {
        mutableStateOf(false)
    }
    var error by remember {
        mutableStateOf<Exception?>(null)
    }
    var onErrorClick by remember {
        mutableStateOf(false)
    }
    var loadingText by remember {
        mutableStateOf("")
    }
    var urls by remember {
        mutableStateOf(emptyList<NameAndUrlModel>())
    }
    AlertDialog(confirmButton = {},
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .animateContentSize(),
        properties = DialogProperties(
            usePlatformDefaultWidth = false, dismissOnClickOutside = false
        ),
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        },
        text = {
            Column(Modifier.fillMaxWidth()) {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = stringResource(id = R.string.loading),
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
                    Spacer(modifier = Modifier.height(7.dp))
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (error != null) {
                    Text(
                        text = error!!.localizedMessage ?: "Unknown error occurs",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(7.dp))

                    MyButton(
                        text = stringResource(id = R.string.retry),
                        modifier = Modifier.fillMaxWidth(),
                        painter = painterResource(id = R.drawable.ic_retry)
                    ) {
                        onErrorClick = !onErrorClick
                    }
                    MyButton(
                        text = stringResource(id = R.string.view_in_browser),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Ym.download(context = context, downloadItem.url)
                    }

                } else {
                    Text(
                        text = downloadItem.title,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(7.dp))

                    for (item in urls) {
                        val text = if (item.text.contains("Download", ignoreCase = true)) {
                            item.text
                        } else {
                            "Download ${item.text}"
                        }
                        MyButton(text = text, modifier = Modifier.fillMaxWidth()) {
                            onDownLoad(
                                downloadItem.copy(
                                    url = item.url, more = listOf(item.text)
                                )
                            )
                        }
                    }
                }
            }
        })
    LaunchedEffect(key1 = onErrorClick) {
        withContext(Dispatchers.IO) {
            getMSexLinks(downloadItem.url).myCollect(onloading = {
                isLoading = true
                if (error != null) error = null
            }, onerror = {
                isLoading = false
                error = it
            }, onsuccess = {
                isLoading = false
                urls = it
            })
        }
    }
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
private fun YtDia(
    actionForMyanmar: String,
    downloadItem: DownloadItem,
    onDismiss: () -> Unit,
    onDownLoad: (DownloadItem) -> Unit,
    context: Context = LocalContext.current,
) {
    var ytlinks by remember {
        mutableStateOf<YoutubeDownloadModel?>(null)
    }
    var embedUrl by remember {
        mutableStateOf<String?>(null)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var onErrorClick by remember {
        mutableStateOf(false)
    }
    var error by remember {
        mutableStateOf<Exception?>(null)
    }
    var loadingText by remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false, dismissOnClickOutside = false
        ),
        modifier = Modifier.fillMaxWidth(0.95f),
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        },
        text = {
            Column(Modifier.fillMaxWidth()) {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = stringResource(id = R.string.loading),
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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (error != null) {
                    Text(
                        text = error!!.localizedMessage ?: "Unknown error occurs",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    MyButton(
                        text = stringResource(id = R.string.retry),
                        modifier = Modifier.fillMaxWidth(),
                        painter = painterResource(id = R.drawable.ic_retry)
                    ) {
                        onErrorClick = !onErrorClick
                        error=null
                    }
                    if (embedUrl != null) {
                        MyButton(
                            text = stringResource(id = R.string.view_in_yt),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Ym.download(context = context, embedUrl!!)
                        }
                    } else {
                        MyButton(
                            text = stringResource(id = R.string.view_in_browser),
                            painter = painterResource(id = R.drawable.ic_network),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Ym.download(context = context, downloadItem.url)
                        }
                    }


                } else if (ytlinks != null) {
                    MyAsyncImage(
                        thumbUrl = ytlinks!!.thumb,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f)
                    )
                    if (ytlinks!!.meta.duration.isNotBlank()) {
                        Text(
                            text = "Duration : ${ytlinks!!.meta.duration}",
                            modifier = Modifier.padding(7.dp)
                        )
                    }
                    for (meta in ytlinks!!.url) {
                        val size = if (meta.filesize == 0) {
                            "Unknown size"
                        } else {
                            Formatter.formatShortFileSize(context, meta.filesize.toLong())
                        }
                        val quality = "${meta.quality}p"
                        MyButton(text = "$quality( $size )", modifier = Modifier.fillMaxWidth()) {
                            onDownLoad(
                                downloadItem.copy(
                                    more = listOf(
                                        "Quality : $quality", "Size : $size"
                                    ), url = meta.url
                                )
                            )
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
    LaunchedEffect(key1 = onErrorClick) {
        withContext(Dispatchers.IO) {
            if (embedUrl.isNullOrEmpty()) {
                getMmEmbedLink(actionForMyanmar).myCollect(onloading = {
                    isLoading = true
                    if (error != null) error = null
                }, onerror = {
                    isLoading = false
                    error = it
                }, onsuccess = {
                    embedUrl = it
                })
            }
            if (embedUrl != null && error == null) {
                LinkGenerator.generateYtLinks(embedUrl!!).myCollect(onloading = {
                    if (!isLoading) isLoading = true
                    if (error != null) error = null
                }, onerror = {
                    isLoading = false
                    error = it
                }, onsuccess = {
                    isLoading = false
                    ytlinks = it
                })
            }


        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MsubSeriesDownDia(
    title: String,
    data: GcSeriesDownloadDataItem,
    onDismiss: () -> Unit,
    onDownload: (NameAndUrlModel) -> Unit,
) {
    var isLoading by remember {
        mutableStateOf(true)
    }
    var error by remember {
        mutableStateOf<Exception?>(null)
    }
    var onErrorClick by remember {
        mutableStateOf(false)
    }
    var downloadlinks by remember {
        mutableStateOf<List<NameAndUrlModel>?>(null)
    }

    if (isLoading) {
        LoadingDia(onCancel = onDismiss, downloadInBrowser = {}, showConfirmBt = false)
    } else if (error != null) {
        ErrorDia(orginalUrl = data.url,
            error = error!!,
            onDismiss = onDismiss,
            onErrorClick = { onErrorClick = !onErrorClick })
    } else if (downloadlinks == null) {
        ErrorDia(orginalUrl = data.url,
            error = Exception("Unknown error occurs"),
            onDismiss = onDismiss,
            onErrorClick = { onErrorClick = !onErrorClick })
    } else {
        AlertDialog(properties = DialogProperties(
            dismissOnClickOutside = false, usePlatformDefaultWidth = false
        ),
            modifier = Modifier.fillMaxWidth(0.95f),
            onDismissRequest = onDismiss,
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            },
            title = {
                Text(text = title)
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (data.thumb.isNotEmpty()) {
                            MyAsyncImage(
                                thumbUrl = data.thumb,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(60.dp)
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            if (data.title.isNotEmpty()) {
                                Text(text = "Season : ${data.title}")
                            }
                            if (data.episode.isNotEmpty()) {
                                Text(text = "Episode : ${data.episode}")
                            }
                            if (data.date.isNotEmpty()) {
                                Text(text = "Date : ${data.date}")
                            }
                        }


                    }
                    MyDivider(modifier = Modifier.padding(vertical = 5.dp))
                    for (download in downloadlinks!!) {
                        MyButton(text = download.text, modifier = Modifier.fillMaxWidth()) {
                            onDismiss()
                            onDownload(download)
                        }
                    }
                }
            })
    }

    LaunchedEffect(key1 = onErrorClick) {
        withContext(Dispatchers.IO) {
            isLoading = true
            if (error != null) error = null
            val document = Jsoup.connect(data.url).get()


            val downlinks = document.getElementsByClass("mybutton").map {
                val text = if (it.text().contains("here", ignoreCase = true)) {
                    it.text().replace("here", "", ignoreCase = true)
                } else {
                    it.text()
                }
                NameAndUrlModel(
                    text = text, url = it.attr("href")
                )
            }
            isLoading = false
            downloadlinks = downlinks
        }
    }
}

suspend fun getMmEmbedLink(actionForMyanmar: String): Flow<Response<String>> {
    return flow {
        try {
            emit(Response.Loading())
            val adminLink = "https://myanmarsubmovie.com/wp-admin/admin-ajax.php"
            val document =
                Jsoup.connect(adminLink).requestBody(actionForMyanmar).ignoreContentType(true)
                    .post()
            val obj = JSONObject(document.body().text())
            val embedUrl = obj.getString("embed_url").replace("\\\\", "")
            emit(Response.Success(embedUrl))
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }
}

suspend fun getMSexLinks(url: String): Flow<Response<List<NameAndUrlModel>>> {
    return flow {
        try {
            emit(Response.Loading())
            val document = Jsoup.connect(url).get()
            val links = document.getElementsByClass("wp-block-button__link").map {
                NameAndUrlModel(
                    text = it.getElementsByTag("strong").text(), url = it.attr("href")
                )
            }
            if (links.isEmpty()) {
                emit(Response.Error(Exception("Unknown error occurs")))
            } else {
                emit(Response.Success(links))
            }
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }
}

@Composable
fun DownOrWatchDia(
    item: DownloadItem,
    context: Context = LocalContext.current,
    onDismiss: () -> Unit,
    onDownLoad: (String) -> Unit,
) {
    AlertDialog(modifier = Modifier.fillMaxWidth(0.95f),
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        ),
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        },
        confirmButton = {},
        title = {
            Text(text = item.title)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(7.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    MyAsyncImage(
                        thumbUrl = item.thumb,
                        modifier = Modifier
                            .fillMaxWidth(0.35f)
                            .aspectRatio(0.65f)
                    )
                    Column {
                        if (item.serverIcon.isNotEmpty()) {
                            Row(
                                modifier = Modifier.padding(5.dp),
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                AsyncImage(
                                    model = item.serverIcon,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(
                                            RoundedCornerShape(7.dp)
                                        )
                                )

                                Text(text = item.server)
                            }
                        } else {
                            if (item.server.isNotEmpty()) {
                                Text(text = item.server)
                            }


                        }

                        for (text in item.more) {
                            Text(text = text, modifier = Modifier.padding(5.dp))
                        }
                    }
                }

                FilledTonalButton(
                    onClick = {
                        onDismiss()
                        onDownLoad(item.url)
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_down),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = stringResource(id = R.string.download))
                }
                FilledTonalButton(onClick = {
                    onDismiss()
                    Goto.goWatch(
                        context, title = item.title, url = item.url
                    )
                }, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye), contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = stringResource(id = R.string.watch))
                }
                FilledTonalButton(onClick = {
                    onDismiss()
                    Ym.copyLink(context, item.url)
                }, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_copy_link),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = stringResource(id = R.string.copy_link))
                }
            }
        })

}