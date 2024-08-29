package com.ym.yourmovies.ui.main

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.others.Ym
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup

@Composable
fun SplashScreen(
    onSplashFinish: () -> Unit,
) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 2f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
    }
    var updateState by remember {
        mutableStateOf<UpdateState?>(null)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.ic_ym_circle),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .scale(scale.value)
        )
    }
    if (updateState != null) {
        val isDissmisable = updateState?.force != true
        val context = LocalContext.current
        AlertDialog(onDismissRequest = {
            if (isDissmisable){
                onSplashFinish()
                updateState = null
            }
        },
            confirmButton = {
                TextButton(onClick = {
                    if (isDissmisable){
                        onSplashFinish()
                        updateState = null
                    }
                    Ym.download(context, updateState!!.appUrl)
                }) {
                    Text(text = stringResource(id = R.string.update))
                }
            },
            dismissButton = {
                if (isDissmisable) {
                    TextButton(onClick = {
                        onSplashFinish()
                        updateState = null
                    }) {
                        Text(text = stringResource(id = R.string.not_now))
                    }
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ym_circle),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(100.dp)
                )
            },
            title = { Text(text = updateState?.title?:"") },
            text = { Text(text = updateState?.description?:"") })
    }
    LaunchedEffect(key1 = true) {
        withContext(Dispatchers.IO) {
            try {
                val document =
                    Jsoup.connect("https://raw.githubusercontent.com/SiriusFtTpw1/AppUpdates/main/updates")
                        .get()
                val jsonObj = JSONObject(document.body().html()).getJSONObject("ym")
                val version = jsonObj.getInt("version")
                if (version > 2) {
                    val force = jsonObj.getBoolean("force")
                    val title = jsonObj.getString("title")
                    val description = jsonObj.getString("description")
                    val appUrl = jsonObj.getString("appUrl")
                    updateState = UpdateState(version, force, title, description, appUrl)

                }else{
                    withContext(Dispatchers.Main){
                        onSplashFinish()
                    }
                }


            } catch (_: Exception) {
                delay(1000)
                withContext(Dispatchers.Main){
                    onSplashFinish()
                }
            }
        }
    }
}

private data class UpdateState(
    val version: Int,
    val force: Boolean,
    val title: String,
    val description: String,
    val appUrl: String,
)