package com.ym.yourmovies.download.ui

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.components.MyButton
import com.ym.yourmovies.utils.others.Util1DM
import com.ym.yourmovies.utils.others.Ym

@Composable
fun ChoiceDialog(
    activity: Activity,
    title: String,
    url: String,
    onDimiss: () -> Unit,
    on1DmDownload: (String) -> Unit,
    onBrowserDownload: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDimiss,
        properties = DialogProperties(dismissOnClickOutside = false),
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDimiss) {
                Text(
                    text = stringResource(id = R.string.dismiss)
                )
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Column {
                Text(text = stringResource(id = R.string.download_with))
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MyButton(onClick = {
                        onDimiss()
                        on1DmDownload(url)
                    }, text = "1 DM")
                    MyButton(onClick = {
                        onDimiss()
                        onBrowserDownload(url)
                    }, text = "Browser")
                }

                MyButton(
                    onClick = {
                        onDimiss()
                        Ym.copyLink(context = activity, url)
                    },
                    painter = painterResource(id = R.drawable.ic_copy_link),
                    text = stringResource(id = R.string.copy_link),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

            }
        }
    )
}