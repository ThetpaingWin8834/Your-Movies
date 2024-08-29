package com.ym.yourmovies.msub.details

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
import com.ym.yourmovies.download.utils.DownHelper
import com.ym.yourmovies.msub.details.movie.ui.MSubMovieDetails
import com.ym.yourmovies.msub.details.series.ui.MsubSeriesDetails
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.ui.theme.YourMoviesTheme
import com.ym.yourmovies.utils.abstracts.AbstractAdActivity
import com.ym.yourmovies.utils.components.DetailsAppbar
import com.ym.yourmovies.utils.components.ads.ApplovinBanner
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.others.Util1DM
import com.ym.yourmovies.utils.others.Ym
import com.ym.yourmovies.utils.others.toMsubMovie
import com.ym.yourmovies.watch_later.model.WlEntity

class MsubDetailsActivity : AbstractAdActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            YourMoviesTheme {
                val movie = intent.getBundleExtra(MyConst.movie)!!.toMsubMovie()
                val snackbarHostState = remember {
                    SnackbarHostState()
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
                val dmMethod = remember {
                    MySettingsManager.downloadWith(this)
                }
                var directLink by remember {
                    mutableStateOf<String?>(null)
                }
                val ondownload = remember<(String) -> Unit> {
                    {
                        DownHelper.downWith(which = dmMethod, activity = this, it, onAsk = {
                            directLink = it
                        })
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
                                MsubSeriesDetails(
                                    movie = movie,
                                    context = this@MsubDetailsActivity,
                                    ondownload = ondownload
                                )
                            } else {
                                MSubMovieDetails(
                                    movie = movie,
                                    context = this@MsubDetailsActivity,
                                    onDownLoad = ondownload
                                )
                            }

                        }
                        ApplovinBanner()
                    }
                    if (!directLink.isNullOrEmpty()) {
                        ChoiceDialog(activity = this, title = movie.title, url = directLink!!, onDimiss = {
                            directLink = null
                        },
                            on1DmDownload = {url->
                                showAd {
                                    Util1DM.downloadFile(this@MsubDetailsActivity, url, false, true)
                                }
                            },
                            onBrowserDownload = {url->
                                showAd{

                                    Ym.download(this@MsubDetailsActivity,url)
                                }
                            })
                    }

                }
            }
        }
    }
}