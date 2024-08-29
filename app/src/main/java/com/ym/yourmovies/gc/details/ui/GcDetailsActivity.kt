package com.ym.yourmovies.gc.details.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.ym.yourmovies.download.ui.ChoiceDialog
import com.ym.yourmovies.download.ui.CmClickEvent
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.ui.settings.dm_1dm
import com.ym.yourmovies.ui.settings.dm_ask
import com.ym.yourmovies.ui.settings.dm_browser
import com.ym.yourmovies.ui.theme.YourMoviesTheme
import com.ym.yourmovies.ui.watch.compose.WatchableDia2
import com.ym.yourmovies.ui.watch.data.WatchServer
import com.ym.yourmovies.ui.watch.model.WatchableItem
import com.ym.yourmovies.utils.abstracts.AbstractAdActivity
import com.ym.yourmovies.utils.components.DetailsAppbar
import com.ym.yourmovies.utils.components.ads.ApplovinBanner
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.utils.others.*
import com.ym.yourmovies.watch_later.model.WlEntity

class GcDetailsActivity : AbstractAdActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            YourMoviesTheme {
                val movie = intent.getBundleExtra(MyConst.movie)!!.toGcMovie()
                val snackbarHostState = remember {
                    SnackbarHostState()
                }
                var watchableList by remember {
                    mutableStateOf(emptyList<WatchableItem>())
                }
                var showWatchDia by remember {
                    mutableStateOf(false)
                }
                val wlEntity = remember {
                    WlEntity(
                        id = 0,
                        title = movie.title,
                        date = "",
                        rating = movie.rating,
                        thumb = movie.thumb,
                        url = movie.url
                    )
                }
                var directLink by remember {
                    mutableStateOf<String?>(null)
                }
                val dmMethod = remember {
                    MySettingsManager.downloadWith(this)
                }
                val onDownloadOrWatch = remember<(CmClickEvent, String, WatchServer) -> Unit> {
                    { event, string, server ->
                        when (event) {
                            CmClickEvent.Download -> {
                                when (server) {
                                    WatchServer.YoteShinDrive, WatchServer.Gdrive, WatchServer.Unknown -> {
                                        showAd {
                                            Ym.download(this, string)
                                        }
                                    }
                                    else -> Unit
                                }
                            }
                            CmClickEvent.Browser -> {
                                showAd {
                                    Ym.download(this, string)
                                }
                            }
                            CmClickEvent.Watch -> {
                                when (server) {
                                    WatchServer.MegaUp, WatchServer.MediaFire -> {
                                        showAd {
                                            Goto.goWatch(this, url = string, title = movie.title)
                                        }
                                    }
                                    WatchServer.YoteShinDrive -> {
                                        showAd {
                                            Ym.launchYoteShinDriveApp(context = this, string)
                                        }
                                    }
                                    WatchServer.Gdrive -> {
                                        showAd {
                                            Ym.download(this, string)
                                        }
                                    }
                                    WatchServer.Unknown -> Unit
                                }

                            }
                            CmClickEvent.DownWithYoteShin -> {
                                showAd {
                                    Ym.launchYoteShinDriveApp(context = this, string)
                                }
                            }
                            CmClickEvent.Copy -> Unit
                        }
                    }
                }
                val onDownloadByDm = remember<(String) -> Unit> {
                    { url ->
                        when (dmMethod) {
                            dm_ask -> {
                                directLink = url
                            }
                            dm_1dm -> {
                                showAd {
                                    Util1DM.downloadFile(this@GcDetailsActivity, url, false, true)
                                }
                            }
                            dm_browser -> {
                                showAd {

                                    Ym.download(this@GcDetailsActivity, url)
                                }
                            }
                        }

                    }
                }
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        DetailsAppbar(
                            title = movie.title,
                            movie = wlEntity,
                            snackbarHostState = snackbarHostState,
                            onBackPressedDispatcher = onBackPressedDispatcher,
                            scrollBehavior = scrollBehavior
                        )
                    },
                    floatingActionButton = {
                        if (watchableList.isNotEmpty()) {
                            FloatingActionButton(onClick = {
                                showWatchDia = true
                            }, content = {
                                Icon(
                                    imageVector = Icons.Rounded.PlayArrow,
                                    contentDescription = null
                                )
                            })
                        }

                    }
                ) { paddings ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddings)
                            .padding(horizontal = 7.dp)
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            val isSeries = movie.url.contains("/tvshows/")
                            if (isSeries) {
                                GcSeriesDetails(
                                    movie = movie,
                                    context = this@GcDetailsActivity,
                                    onDownloadByDm = onDownloadByDm,
                                    onDownloadOrWatch = onDownloadOrWatch
                                )
                            } else {
                                GcMovieDetails(
                                    movie = movie,
                                    context = this@GcDetailsActivity,
                                    watch = { list ->
                                        watchableList = list
                                    },
                                    onDownloadByDm = onDownloadByDm,
                                    onDownloadOrWatch = onDownloadOrWatch
                                )
                            }

                        }
                        ApplovinBanner()
                    }

                }
                if (showWatchDia) {
                    WatchableDia2(
                        title = movie.title,
                        list = watchableList,
                        onDismiss = { showWatchDia = false },
                        channel = Channel.GoldChannel,
                        onWatch = { url, server ->
                            showAd {
                                when (server) {
                                    WatchServer.MegaUp, WatchServer.MediaFire -> {
                                        Goto.goWatch(this, title = movie.title, url = url)
                                    }
                                    WatchServer.Gdrive -> {
                                        Ym.download(this, url)
                                    }
                                    WatchServer.YoteShinDrive -> {
                                        Ym.launchYoteShinDriveApp(this, url)
                                    }
                                    WatchServer.Unknown -> Unit
                                }
                            }
                        })
                }
                if (!directLink.isNullOrEmpty()) {
                    ChoiceDialog(activity = this,
                        title = movie.title,
                        url = directLink!!,
                        onDimiss = {
                            directLink = null
                        },
                        on1DmDownload = { url ->
                            showAd {
                                Util1DM.downloadFile(this@GcDetailsActivity, url, false, true)
                            }
                        },
                        onBrowserDownload = { url ->
                            showAd {

                                Ym.download(this@GcDetailsActivity, url)
                            }
                        })
                }
            }
        }
    }
}