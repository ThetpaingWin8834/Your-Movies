package com.ym.yourmovies.gc.details.ui

import android.app.Activity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ym.yourmovies.R
import com.ym.yourmovies.download.ui.CmClickEvent
import com.ym.yourmovies.download.ui.GcMoviesDownDia
import com.ym.yourmovies.gc.details.models.GcMovieDownloadData
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.ui.watch.data.WatchServer
import com.ym.yourmovies.ui.watch.model.WatchableItem
import com.ym.yourmovies.utils.components.DescriptionTitle
import com.ym.yourmovies.utils.components.LoadingAndError
import com.ym.yourmovies.utils.components.MyBigBackdrops
import com.ym.yourmovies.utils.components.MyDetails
import com.ym.yourmovies.utils.models.DownloadItem
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.checkCanWatch
import com.ym.yourmovies.utils.others.toBundle
import com.ym.yourmovies.utils.others.toWatchModel

@Composable
fun GcMovieDetails(
    modifier: Modifier = Modifier,
    movie: GcMovie,
    context: Activity,
    watch: (List<WatchableItem>) -> Unit,
    viewmodel: GcMovieDetailViewmodel = viewModel(),
    onDownloadOrWatch: (event: CmClickEvent, url: String, watchServer: WatchServer) -> Unit,
    onDownloadByDm: (String) -> Unit
) {
    val data = viewmodel.data.value

    var downloadItem by remember {
        mutableStateOf<DownloadItem?>(null)
    }
    val onDownload = remember<(GcMovieDownloadData) -> Unit> {
        {
            downloadItem = DownloadItem(
                title = movie.title,
                thumb = movie.thumb,
                server = it.quality,
                serverIcon = it.serverIcon,
                more = listOf("Language : ${it.language}", "Size : ${it.size}"),
                url = it.url
            )
        }
    }
    if (data != null) {
        MyDetails(
            modifier = modifier,
            headerData = data.headerData,
            detailsBodyDataList = data.detailsBodyDataList,
            relatedList = data.relatedList,
            iscm = false,
            moreMeta = {},
            backdrops = {
                MyBigBackdrops(backdropList = data.backDropsList, onBackdropClick = {})
            },
            download = {
                watch(checkCanWatch(data.downloadList.map { it.toWatchModel() }))
                GcMovieDownload(downloadList = data.downloadList, onDownload = onDownload)
            },
            onGenreClick = {
                Goto.gcSeeAll(context = context, queryOrUrl = it.url, title = it.text)

            },
            onRelatedClick = {
                Goto.gcDetails(context, it.toBundle())
            }, onCastClick = {
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
    if (downloadItem != null) {
        GcMoviesDownDia(title = movie.title, downloadItem = downloadItem!!, onDismiss = {
            downloadItem = null
        },
           onDownloadOrWatch = onDownloadOrWatch, onDownloadByDm = onDownloadByDm)

    }
}


@Composable
fun GcMovieDownload(
    downloadList: List<GcMovieDownloadData>,
    onDownload: (GcMovieDownloadData) -> Unit,
) {
    if (downloadList.isNotEmpty()) {
        DescriptionTitle(title = stringResource(id = R.string.download))
        GcDownLoadHeader()
        for (download in downloadList) {
            GcDownLoadItem(data = download, onDownload = onDownload)
        }
    }


}

@Composable
fun GcDownLoadHeader(

) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.quality),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.language),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.size),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun GcDownLoadItem(
    data: GcMovieDownloadData,
    onDownload: (GcMovieDownloadData) -> Unit,
) {
    ElevatedButton(
        onClick = { onDownload(data) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp, vertical = 2.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 3.dp
        ),
        shape = RoundedCornerShape(7.dp)
    ) {
        AsyncImage(
            model = data.serverIcon,
            contentDescription = null,
            modifier = Modifier
                .padding(5.dp)
                .size(20.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = data.quality,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            text = data.language,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            text = data.size,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}