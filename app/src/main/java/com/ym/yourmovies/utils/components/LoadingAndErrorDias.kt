package com.ym.yourmovies.utils.components

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.others.LinkNotSupportedException
import com.ym.yourmovies.utils.others.Ym
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jsoup.HttpStatusException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

@Composable
fun LoadingDia(
    title: String = stringResource(id = R.string.loading),
    onCancel: () -> Unit,
    downloadInBrowser: () -> Unit,
    showConfirmBt: Boolean = true,
) {
    var loading by remember {
        mutableStateOf("")
    }
    AlertDialog(
        properties = DialogProperties(dismissOnClickOutside = false),
        onDismissRequest = onCancel,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = loading,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        },
        text = {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
        },
        confirmButton = {
            if (showConfirmBt) {
                TextButton(onClick = downloadInBrowser) {
                    Text(text = stringResource(id = R.string.download_in_browser))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
    LaunchedEffect(key1 = Unit) {
        while (isActive) {
            loading = if (loading.length >= 6) {
                ""
            } else {
                "$loading ."
            }
            delay(500L)
        }
    }
}

@Composable
fun ErrorDia(
    orginalUrl: String,
    error: Exception,
    onDismiss: () -> Unit,
    onErrorClick: () -> Unit,
    context: Context = LocalContext.current
) {
    val painterIcon: Painter
    val title: String
    when (error) {
        is LinkNotSupportedException -> {
            painterIcon = painterResource(id = R.drawable.ic_sad)
            title = stringResource(id = R.string.server_error_occur)
        }
        is UnknownHostException -> {
            painterIcon = painterResource(id = R.drawable.ic_no_conn)
            title = stringResource(id = R.string.try_vpn)
        }
        is SocketTimeoutException, is SSLHandshakeException -> {
            painterIcon = painterResource(id = R.drawable.ic_warning)
            title = stringResource(id = R.string.conn_not_stable)
        }
        is HttpStatusException -> {
            painterIcon = painterResource(id = R.drawable.ic_sad)
            title = stringResource(id = R.string.server_error_occur)
        }
        else -> {
            painterIcon = painterResource(id = R.drawable.ic_sad)
            title = stringResource(id = R.string.unknown_error)
        }
    }
    AlertDialog(onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = true),
        title = {
            Text(text = title)
        },
        icon = {
            Icon(painter = painterIcon, contentDescription = null)
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                MyButton(
                    onClick = onErrorClick,
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.ic_retry),
                    text = stringResource(id = R.string.retry)
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        }
    )
}