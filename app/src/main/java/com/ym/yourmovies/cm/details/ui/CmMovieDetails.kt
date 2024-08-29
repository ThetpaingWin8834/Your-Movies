package com.ym.yourmovies.cm.details.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ym.yourmovies.R
import com.ym.yourmovies.cm.details.data.CmMovieDetailViewmodel
import com.ym.yourmovies.cm.details.models.CmDownloadItem
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.download.ui.CMdownDia
import com.ym.yourmovies.download.ui.CmClickEvent
import com.ym.yourmovies.ui.watch.data.WatchServer
import com.ym.yourmovies.ui.watch.model.Server
import com.ym.yourmovies.ui.watch.model.WatchModel
import com.ym.yourmovies.ui.watch.model.WatchableItem
import com.ym.yourmovies.utils.components.*
import com.ym.yourmovies.utils.models.DownloadItem
import com.ym.yourmovies.utils.models.DownloadState
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.Ym
import com.ym.yourmovies.utils.others.checkCanWatch
import com.ym.yourmovies.utils.others.toBundle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CmMovieDetails(
    modifier: Modifier = Modifier,
    movie: CmMovie,
    context: Context,
    viewmodel: CmMovieDetailViewmodel = viewModel(),
    watch: (List<WatchableItem>) -> Unit,
    onDownloadOrWatch: (event: CmClickEvent, url: String, watchServer: WatchServer) -> Unit,
    onDownloadByDm: (String) -> Unit
) {
    val data = viewmodel.data.value
    var downloadItem by remember {
        mutableStateOf<DownloadItem?>(null)
    }


    if (data != null) {
        MyDetails(
            modifier = modifier,
            headerData = data.header,
            detailsBodyDataList = data.detailsBodyData,
            relatedList = data.relatedList,
            iscm = true,
            moreMeta = {
                if (data.moreData.winMetaData.isNotBlank()) {
                    IconAndText(id = R.drawable.ic_trophy, text = data.moreData.winMetaData)
                }
                if (data.moreData.viewCount.isNotBlank()) {
                    IconAndText(id = R.drawable.ic_eye, text = data.moreData.viewCount)
                }
                if (data.moreData.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(5.dp))
                    MyDivider()
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = data.moreData.description,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light
                    )

                }
            },
            backdrops = {
                MyBigBackdrops(backdropList = data.backDropList.map {
                    NameAndUrlModel(
                        text = it
                    )
                }, onBackdropClick = {})
            },
            download = {
                watch(checkCanWatch(data.downloadList.map {
                    WatchModel(
                        server = Server(serverName = it.serverName, serverIcon = it.serverIcon),
                        url = it.url,
                        quality = it.quality
                    )
                }))
                CmDownLoad(
                    downloadList = data.downloadList,
                    onDownloadClick = {
                        downloadItem = DownloadItem(
                            title = movie.title,
                            thumb = movie.thumb,
                            server = it.serverName,
                            serverIcon = it.serverIcon,
                            more = listOf(
                                "Quality : ${it.quality}",
                                "Size : ${it.size}"
                            ),
                            url = it.url
                        )


                    }
                )
            },
            onGenreClick = {
                Goto.cmSeeAll(context = context, queryOrUrl = it.url, title = it.text)
            },
            onRelatedClick = {
                Goto.cmDetails(context, it.toBundle())
            },
            onCastClick = {
                if (it.url != null && it.url.startsWith("http")) {
                    Goto.cmSeeAll(context = context, queryOrUrl = it.url, title = it.realname)
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
        CMdownDia(
            title = movie.title, downloadItem = downloadItem!!, onDismiss = {
                downloadItem = null
            },
            onDownloadByDm = onDownloadByDm,
            onDownloadOrWatch = onDownloadOrWatch
        )

    }
}

@Composable
fun GdriveDirectLinkDia(
    yoteShinUrl: String,
    state: StateFlow<DownloadState>,
    ondimissOrCancel: () -> Unit,
    onErrorClick: () -> Unit,
    context: Context = LocalContext.current,
) {
    val data by state.collectAsState()
    if (data.isLoading) {
        LoadingDia(
            onCancel = ondimissOrCancel, downloadInBrowser = {
                ondimissOrCancel()
                Ym.download(context, yoteShinUrl)
            },
            title = stringResource(id = R.string.generating_gdrive_link)
        )
    } else if (data.error != null) {
        ErrorDia(
            orginalUrl = yoteShinUrl,
            error = data.error!!,
            onDismiss = ondimissOrCancel,
            onErrorClick = onErrorClick
        )
    } else {
        AlertDialog(onDismissRequest = { /*TODO*/ },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = ondimissOrCancel) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            },
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_tada), contentDescription = null)
            },
            title = {
                Text(text = stringResource(id = R.string.tada))
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.link_ready_to_download))
                    ElevatedButton(
                        onClick = {
                            ondimissOrCancel()
                            Ym.download(context, data.generatedLink)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(text = stringResource(id = R.string.open_in_gdrive))
                    }
                    ElevatedButton(
                        onClick = {
                            ondimissOrCancel()
                            Ym.copyLink(context, data.generatedLink)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(text = stringResource(id = R.string.copy_link))
                    }
                }
            })
    }
}

@Composable
private fun DownloadHeader() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.server),
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.size),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.quality),
            modifier = Modifier.weight(1f)
        )


    }

}

@Composable
private fun DownloadItem(
    item: CmDownloadItem,
    onDownloadClick: (CmDownloadItem) -> Unit,
) {

    FilledTonalButton(
        onClick = { onDownloadClick(item) },
        shape = RoundedCornerShape(7.dp),
        modifier = Modifier.padding(horizontal = 5.dp)
    ) {

        AsyncImage(
            model = item.serverIcon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = item.serverName,
            modifier = Modifier
                .weight(1.5f)
                .padding(start = 5.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis

        )
        Text(
            text = item.size,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            text = item.quality,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
private fun CmDownLoad(
    downloadList: List<CmDownloadItem>,
    onDownloadClick: (CmDownloadItem) -> Unit,
) {
    if (downloadList.isNotEmpty()) {
        DescriptionTitle(title = stringResource(id = R.string.download))
        DownloadHeader()
        for (item in downloadList) {
            DownloadItem(item = item, onDownloadClick = onDownloadClick)

        }
    }


}