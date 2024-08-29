package com.ym.yourmovies.ui.watch

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView.SHOW_BUFFERING_ALWAYS
import com.ym.yourmovies.R
import com.ym.yourmovies.ui.theme.YourMoviesTheme
import com.ym.yourmovies.utils.others.MyConst

class WatchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
      //  WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val videoUrl = intent.getStringExtra(MyConst.url)
        val movieTitle =
            intent.getStringExtra(MyConst.title) ?: System.currentTimeMillis().toString()
        if (videoUrl.isNullOrEmpty()) {
            Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_LONG).show()
            finish()
        }
        setContent {
            YourMoviesTheme {
                val exoPlayer: ExoPlayer = remember(this) {
                    ExoPlayer.Builder(this)
                        .build()
                        .apply {
                            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
                            playWhenReady = true
                            prepare()
                        }
                }
                val lifecycleOwner = LocalLifecycleOwner.current
                var currentLifeCycle by remember {
                    mutableStateOf(Lifecycle.Event.ON_CREATE)
                }
                var showTitle by remember {
                    mutableStateOf(false)
                }
                var currZoomMode by remember {
                    mutableStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT)
                }
                hideSystemBars()
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(Color.Black)
                DisposableEffect(key1 = lifecycleOwner, effect = {
                    val observer = LifecycleEventObserver { _, event ->
                        currentLifeCycle = event
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                        exoPlayer.release()
                    }
                })
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    AndroidView(
                        factory = {
                            StyledPlayerView(it).apply {
                                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
                                this.layoutParams = layoutParams
                                setShowShuffleButton(false)
                                setShowShuffleButton(false)
                                setShowNextButton(false)
                                setShowPreviousButton(false)
                                setShowVrButton(false)
                                setShowBuffering(SHOW_BUFFERING_ALWAYS)
                                setShowMultiWindowTimeBar(true)

                                player = exoPlayer
                                setFullscreenButtonClickListener { showFull ->
                                    if (showFull) {
                                        hideSystemBars()
                                        enterLandScape()
                                    } else {
                                        showSystemBars()
                                        enterPortrait()
                                    }
                                }
                                setControllerVisibilityListener(StyledPlayerView.ControllerVisibilityListener { visivility ->
                                    if (visivility == View.VISIBLE) {
                                        showTitle = true
                                        showSystemBars()
                                    } else {
                                        showTitle = false
                                        hideSystemBars()
                                    }

                                })
                            }
                        },
                        update = {
                            when (currentLifeCycle) {
                                Lifecycle.Event.ON_RESUME -> {
                                    it.onResume()
                                }
                                Lifecycle.Event.ON_PAUSE -> {
                                    it.onPause()
                                }
                                else -> Unit
                            }
                            it.resizeMode = currZoomMode
                        }, modifier = Modifier.fillMaxSize()
                    )
                    AnimatedVisibility(
                        visible = showTitle,
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    ) {
                        TitleBar(
                            title = movieTitle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .align(
                                    Alignment.TopStart
                                ),
                            onZoomClick = {
                                onZoom(currZoom = currZoomMode){newZoom->
                                    currZoomMode = newZoom
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun enterLandScape() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun enterPortrait() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private fun hideSystemBars() {
        val windowInsetsControllerCompat =
            WindowCompat.getInsetsController(window, window.decorView)
        WindowCompat.setDecorFitsSystemWindows(window,false)
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsControllerCompat.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE

//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
    }

    private fun showSystemBars() {
        val windowInsetsControllerCompat =
            WindowCompat.getInsetsController(window, window.decorView)
        WindowCompat.setDecorFitsSystemWindows(window,true)

        windowInsetsControllerCompat.show(WindowInsetsCompat.Type.systemBars())
//        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    @Composable
    fun TitleBar(
        modifier: Modifier,
        title: String,
        onZoomClick: () -> Unit,
    ) {
        Row(
            modifier = modifier.background(Color.Black.copy(alpha = 0.3f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressedDispatcher::onBackPressed) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.LightGray,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onZoomClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_zoom),
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }
        }
    }

    private fun onZoom(currZoom: Int, onChange: (Int) -> Unit) {
        when (currZoom) {
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> {
                onChange(AspectRatioFrameLayout.RESIZE_MODE_FIT)
            }
            AspectRatioFrameLayout.RESIZE_MODE_FIT -> {
                onChange(AspectRatioFrameLayout.RESIZE_MODE_FILL)

            }
            AspectRatioFrameLayout.RESIZE_MODE_FILL -> {
                onChange(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)

            }
            AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH -> {
                onChange(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT)

            }
            AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT -> {
                onChange(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                val controllerCompat = WindowInsetsControllerCompat(window, window.decorView)
                controllerCompat.show(WindowInsetsCompat.Type.systemBars())
            }
            else -> {
                val controllerCompat = WindowInsetsControllerCompat(window, window.decorView)
                controllerCompat.hide(WindowInsetsCompat.Type.systemBars())
            }
        }
    }
}