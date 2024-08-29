package com.ym.yourmovies.gc.details.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ym.yourmovies.R
import com.ym.yourmovies.download.ui.CmClickEvent
import com.ym.yourmovies.download.ui.GcMoviesDownDia
import com.ym.yourmovies.download.ui.GcSeriesDownDia
import com.ym.yourmovies.gc.details.models.GcSeriesDownloadData
import com.ym.yourmovies.gc.details.models.GcSeriesDownloadDataItem
import com.ym.yourmovies.gc.details.models.GcSeriesDownloadHeader
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.ui.watch.data.WatchServer
import com.ym.yourmovies.utils.components.DescriptionTitle
import com.ym.yourmovies.utils.components.LoadingAndError
import com.ym.yourmovies.utils.components.MyAsyncImage
import com.ym.yourmovies.utils.components.MyBigBackdrops
import com.ym.yourmovies.utils.components.MyDetails
import com.ym.yourmovies.utils.components.MyDivider
import com.ym.yourmovies.utils.models.DownloadItem
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.toBundle
import com.ym.yourmovies.utils.others.toWatchModel

@Composable
fun GcSeriesDetails(
    modifier: Modifier = Modifier,
    movie: GcMovie,
    context: Activity,
    viewmodel: GcSeriesDetailsViewModel = viewModel(),
    onDownloadOrWatch: (event: CmClickEvent, url: String, watchServer: WatchServer) -> Unit,
    onDownloadByDm: (String) -> Unit,
) {
    val data = viewmodel.data.value
    var downloadItem by remember {
        mutableStateOf<DownloadItem?>(null)
    }
    var downlinksData by remember {
        mutableStateOf<GcSeriesDownloadDataItem?>(null)
    }
    val onDownload = remember<(GcSeriesDownloadDataItem) -> Unit> {
        {
            downlinksData = it
        }
    }
    if (data != null) {
        MyDetails(
            modifier = modifier,
            headerData = data.headerData,
            detailsBodyDataList = data.bodyList,
            relatedList = data.relatedList,
            iscm = false,
            moreMeta = {
                for (meta in data.moreData) {
                    Spacer(modifier = Modifier.height(3.dp))
                    if (meta.title.isNotBlank() && meta.text.isNotBlank()) {
                        Row {
                            Text(
                                text = meta.title,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = HtmlCompat.fromHtml(
                                    meta.text,
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                ).toString(),
                                fontWeight = FontWeight.Light,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            },
            backdrops = {
                MyBigBackdrops(backdropList = data.backdropList, onBackdropClick = {})
            },
            download = {
                GcSeriesDetailsDownload(list = data.downloadDataList, onDlClick = onDownload)
            },
            onRelatedClick = { Goto.gcDetails(context, it.toBundle()) },
            onGenreClick = {
                Goto.gcSeeAll(context = context, queryOrUrl = it.url, title = it.text)

            },
            onCastClick = {
                if (it.url != null && it.url.startsWith("http")) {
                    Goto.gcSeeAll(context = context, queryOrUrl = it.url, title = it.realname)
                }
            }

        )
    }
    LoadingAndError(
        isLoading = viewmodel.isLoading,
        error = viewmodel.error,
        onErrorClick = {
            viewmodel.getData(movie)
        })
    LaunchedEffect(key1 = true) {
        viewmodel.getData(movie)
    }
    if (downlinksData != null) {
        GcSeriesDownDia(
            title = movie.title,
            data = downlinksData!!,
            onDismiss = { downlinksData = null },
            onDownload = {
                val watchModel = it.toWatchModel()
                downlinksData = null
                downloadItem = DownloadItem(
                    title = movie.title,
                    thumb = movie.thumb,
                    server = watchModel.server.serverName,
                    serverIcon = it.serverIcon,
                    more = listOf(
                        "Quality : ${watchModel.quality}",
                        "Size : ${it.size}",
                        "Language : ${it.language}"
                    ),
                    url = it.url
                )
            }
        )
    }
    if (downloadItem != null) {
        GcMoviesDownDia(
            title = movie.title, downloadItem = downloadItem!!, onDismiss = {
                downloadItem = null
            },
            onDownloadByDm = onDownloadByDm, onDownloadOrWatch = onDownloadOrWatch
        )
    }
}


@Composable
fun GcSeriesDetailsDownload(
    list: List<GcSeriesDownloadData>,
    onDlClick: (GcSeriesDownloadDataItem) -> Unit,

    ) {
    if (list.isNotEmpty()) {
        DescriptionTitle(title = stringResource(id = R.string.download))
        for (data in list) {
            GcSeriesDetailsDownloadItemSeason(data = data, onDlClick)
        }
    }

}

@Composable
private fun GcSeriesDetailsDownloadItemSeason(
    data: GcSeriesDownloadData,
    onDlClick: (GcSeriesDownloadDataItem) -> Unit,

    ) {
    GcSeriesDetailsDownloadHeader(header = data.header)
    for (item in data.downloadList) {
        Spacer(modifier = Modifier.height(3.dp))
        GcSeriesDetailsDownloadItemEpisode(item = item, onDlClick)
    }
}

@Composable
private fun GcSeriesDetailsDownloadHeader(
    header: GcSeriesDownloadHeader,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = header.seasonIndex,
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(5.dp))
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            style = MaterialTheme.typography.labelLarge

        )
        Text(
            text = header.title,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = header.date,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(3.dp))
    MyDivider()

}

@Composable
private fun GcSeriesDetailsDownloadItemEpisode(
    item: GcSeriesDownloadDataItem,
    onDlClick: (GcSeriesDownloadDataItem) -> Unit,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onDlClick(item)
        }) {
        MyAsyncImage(
            thumbUrl = item.thumb, modifier = Modifier
                .width(100.dp)
                .height(60.dp)
        )
        Text(
            text = item.title,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .wrapContentHeight()
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = item.episode,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = item.date,
                maxLines = 1,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
    }
}