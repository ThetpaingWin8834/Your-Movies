package com.ym.yourmovies.download.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ym.yourmovies.download.utils.LinkGenerator
import com.ym.yourmovies.gc.details.models.GcMovieDownloadData
import com.ym.yourmovies.utils.components.ErrorDia
import com.ym.yourmovies.utils.components.LoadingDia
import com.ym.yourmovies.utils.models.DownloadItem
import com.ym.yourmovies.utils.others.myCollect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import  com.ym.yourmovies.R
import com.ym.yourmovies.gc.details.models.GcSeriesDownloadDataItem
import com.ym.yourmovies.gc.details.ui.GcDownLoadHeader
import com.ym.yourmovies.gc.details.ui.GcDownLoadItem
import com.ym.yourmovies.ui.watch.data.WatchServer
import com.ym.yourmovies.utils.components.MyAsyncImage
import com.ym.yourmovies.utils.components.MyDivider


@Composable
fun GcMoviesDownDia(
    title: String,
    downloadItem: DownloadItem,
    onDismiss: () -> Unit,
    onDownloadByDm: (String) -> Unit,
    onDownloadOrWatch: (event: CmClickEvent, url: String, watchServer: WatchServer) -> Unit
    ) {
    var redirectLoading by remember {
        mutableStateOf(true)
    }
    var redirectError by remember {
        mutableStateOf<Exception?>(null)
    }
    var onErrorClick by remember {
        mutableStateOf(false)
    }
    var redirectedLink by remember {
        mutableStateOf<String?>(null)
    }
    if (redirectLoading) {
        LoadingDia(onCancel = onDismiss, downloadInBrowser = {}, showConfirmBt = false)
    } else if (redirectError != null) {
        ErrorDia(
            orginalUrl = downloadItem.url,
            error = redirectError!!,
            onDismiss = onDismiss,
            onErrorClick = { onErrorClick = !onErrorClick })
    } else if (redirectedLink.isNullOrEmpty()) {
        ErrorDia(
            orginalUrl = downloadItem.url,
            error = Exception("Unknown error occurs"),
            onDismiss = onDismiss,
            onErrorClick = { onErrorClick = !onErrorClick })
    } else {
        CMdownDia(
            title = title,
            downloadItem = downloadItem.copy(url = redirectedLink!!),
            onDismiss = onDismiss,
            onDownloadByDm = onDownloadByDm,
            onDownloadOrWatch = onDownloadOrWatch
        )
    }

    LaunchedEffect(key1 = onErrorClick) {
        withContext(Dispatchers.IO) {
            LinkGenerator.getRedirectLinkFrom(downloadItem.url)
                .myCollect(
                    onloading = {
                        redirectLoading = true
                        if (redirectError != null) redirectError = null
                    },
                    onerror = {
                        redirectLoading = false
                        redirectError = it
                    },
                    onsuccess = {
                        redirectLoading = false
                        redirectedLink = it
                    }
                )
        }
    }
}

@Composable
fun GcSeriesDownDia(
    title: String,
    data: GcSeriesDownloadDataItem,
    onDismiss: () -> Unit,
    onDownload: (GcMovieDownloadData) -> Unit,
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
        mutableStateOf<List<GcMovieDownloadData>?>(null)
    }

    if (isLoading) {
        LoadingDia(onCancel = onDismiss, downloadInBrowser = {}, showConfirmBt = false)
    } else if (error != null) {
        ErrorDia(
            orginalUrl = data.url,
            error = error!!,
            onDismiss = onDismiss,
            onErrorClick = { onErrorClick = !onErrorClick })
    } else if (downloadlinks == null) {
        ErrorDia(
            orginalUrl = data.url,
            error = Exception("Unknown error occurs"),
            onDismiss = onDismiss,
            onErrorClick = { onErrorClick = !onErrorClick })
    } else {
        AlertDialog(
            properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false),
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
                                thumbUrl = data.thumb, modifier = Modifier
                                    .width(100.dp)
                                    .height(60.dp)
                            )
                        }
                        Column (verticalArrangement = Arrangement.spacedBy(5.dp)) {
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
                    GcDownLoadHeader()
                    for (download in downloadlinks!!) {
                        GcDownLoadItem(data = download, onDownload = onDownload)
                    }
                }
            }
        )
    }

    LaunchedEffect(key1 = onErrorClick) {
        withContext(Dispatchers.IO) {
            isLoading = true
            if (error != null) error = null
            val document = Jsoup.connect(data.url).followRedirects(true).get()
            val downloadLinks = mutableListOf<GcMovieDownloadData>()
            document.select("#download tr").forEachIndexed { index, element ->
                if (index != 0) {
                    downloadLinks.add(
                        GcMovieDownloadData(
                            serverIcon = element.getElementsByTag("img").attr("src"),
                            quality = element.getElementsByClass("quality").text(),
                            language = element.getElementsByTag("td")[2].text(),
                            size = element.getElementsByTag("td")[3].text(),
                            url = element.getElementsByTag("a").attr("href")
                        )
                    )
                }
            }
            isLoading = false
            downloadlinks = downloadLinks
        }
    }
}