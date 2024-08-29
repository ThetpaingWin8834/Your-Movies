package com.ym.yourmovies.utils.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.ym.yourmovies.utils.others.Ym

@Composable
fun YourMoviesDownloadDia(
    item: DownloadItem,
    context: Context = LocalContext.current,
    onDismiss: () -> Unit,
    onDownLoad : (DownloadItem)->Unit
) {

    AlertDialog(onDismissRequest = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        },
        confirmButton = {},
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
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onDownLoad(item)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_down),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(id = R.string.download))
                }
                OutlinedButton(
                    onClick = {
                        onDismiss()
                       Ym.download(context,item.url)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_earth),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(id = R.string.view_in_browser))
                }
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        Ym.copyLink(context = context, link = item.url)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_copy_link),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(id = R.string.copy_link))
                }


            }
        })


}