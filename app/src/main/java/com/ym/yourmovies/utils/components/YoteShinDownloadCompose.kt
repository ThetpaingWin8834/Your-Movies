package com.ym.yourmovies.utils.components

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.models.DownloadItem
import com.ym.yourmovies.utils.models.DownloadState
import com.ym.yourmovies.utils.others.LinkNotSupportedException
import com.ym.yourmovies.utils.others.Ym
import kotlinx.coroutines.flow.StateFlow
import java.net.UnknownHostException

@Composable
fun YoteShinDownloadCompose(
    item: DownloadItem,
    state: StateFlow<DownloadState>,
    context: Context = LocalContext.current,
    onDismiss: () -> Unit,
    onGererateDrive: (String) -> Unit,
    onErrorClick: () -> Unit
) {
    val downloadState by state.collectAsState()


    if (downloadState.isLoading) {
        LoadingDia(onCancel = onDismiss, downloadInBrowser = {
            onDismiss()
            Ym.download(context, item.url)
        })
    } else if (downloadState.error != null) {
        ErrorDia(
            orginalUrl = item.url,
            error = downloadState.error!!,
            onDismiss = onDismiss,
            onErrorClick = onErrorClick
        )

    } else {
        AlertDialog(onDismissRequest = {},
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            },
            icon = {
            },
            title = {
                Text(text = item.title)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
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
                        Spacer(modifier = Modifier.width(5.dp))
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
                    ElevatedButton(
                        onClick = {
                            onDismiss()
                            Ym.launchYoteShinDriveApp(
                                context = context,
                                downloadState.generatedLink
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(text = stringResource(id = R.string.download_in_yoteshin_drive))
                    }
                    ElevatedButton(
                        onClick = {
                            onDismiss()
                            onGererateDrive(item.url)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(text = stringResource(id = R.string.generate_drive_link))
                    }
                    ElevatedButton(
                        onClick = {
                            onDismiss()
                            Ym.download(context = context, link = item.url)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(text = stringResource(id = R.string.view_in_browser))
                    }
                }
            })

    }

}