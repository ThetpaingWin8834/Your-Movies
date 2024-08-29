package com.ym.yourmovies.ui.main

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.ym.yourmovies.R
import com.ym.yourmovies.YmApp
import com.ym.yourmovies.ui.MainScreen2
import com.ym.yourmovies.ui.main.maindrawer.ChannelDesc
import com.ym.yourmovies.ui.main.maindrawer.ChannelItem
import com.ym.yourmovies.ui.main.maindrawer.DrawerHeader
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.ui.theme.YourMoviesTheme
import com.ym.yourmovies.utils.components.CategoriesCompose
import com.ym.yourmovies.utils.components.ads.ApplovinBanner
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.others.Ym
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.concurrent.TimeUnit
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MySettingsManager.upLauguage(newBase))
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            YourMoviesTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val viewModel: MainViewModel = viewModel()
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                val scope = rememberCoroutineScope()

                var cateList by remember {
                    mutableStateOf(emptyList<CategoryItem>())
                }
                var isShowCate by remember {
                    mutableStateOf(false)
                }
                val currentRoute by viewModel.currChannel.collectAsState()
                val rotate by animateFloatAsState(targetValue = if (isShowCate) -180f else 0f)

                val onNavItemClick = remember<(ChannelRoute) -> Unit> {
                    {
                        if (isShowCate) isShowCate = false
                        viewModel.onCurrentChannelChange(it)
                        scope.launch {
                            drawerState.close()
                        }
                    }
                }

                ModalNavigationDrawer(
                    gesturesEnabled = currentRoute !=ChannelRoute.Splash,
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .statusBarsPadding()
                        ) {
                            LazyColumn {
                                item(key = "header") {
                                    DrawerHeader()
                                }
                                item(
                                    key = "chann_desc"
                                ) {
                                    ChannelDesc(text = stringResource(id = R.string.channels))
                                }
                                item {
                                    ChannelItem(title = stringResource(id = R.string.channel_myanmar),
                                        isselected = currentRoute == ChannelRoute.ChannelMyanmar,
                                        onChannelItemClick = { onNavItemClick(ChannelRoute.ChannelMyanmar) })
                                }
                                item {
                                    ChannelItem(title = stringResource(id = R.string.gold_channel),
                                        isselected = currentRoute == ChannelRoute.GoldChannel,
                                        onChannelItemClick = { onNavItemClick(ChannelRoute.GoldChannel) })
                                }
                                item {
                                    ChannelItem(title = stringResource(id = R.string.mm_sub_movie),
                                        isselected = currentRoute == ChannelRoute.MyanmarSubtitles,
                                        onChannelItemClick = { onNavItemClick(ChannelRoute.MyanmarSubtitles) })
                                }

                                item {
                                    ChannelDesc(text = stringResource(id = R.string.miscellaneous))
                                }
                                item {
                                    ChannelItem(title = stringResource(id = R.string.settings),
                                        isselected = currentRoute == ChannelRoute.Settings,
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Rounded.Settings,
                                                contentDescription = null
                                            )
                                        },
                                        onChannelItemClick = { onNavItemClick(ChannelRoute.Settings) }
                                    )
                                }
                            }
                        }

                    }
                ) {
                    val shouldShowAppBar by remember {
                        derivedStateOf { currentRoute != ChannelRoute.Splash }
                    }
                    Scaffold(
                        topBar = {
                            AnimatedVisibility(
                                visible = shouldShowAppBar,
                                enter = fadeIn() + slideInVertically()
                            ) {
                                MainAppBar(
                                    onCateClick = {
                                        if (cateList.isNotEmpty()) {
                                            isShowCate = !isShowCate
                                        }
                                    },
                                    cateRotation = rotate,
                                    onNavigationClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                    scrollBehavior = scrollBehavior,
                                    channelName = if (currentRoute == ChannelRoute.Splash) {
                                        ""
                                    } else {
                                        currentRoute.name
                                    }
                                )
                            }
                        },

                        ) { paddingValues ->

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection)
                                .padding(paddingValues),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                val isLinear = remember {
                                    MySettingsManager.getIsLinear(this@MainActivity)
                                }
                                val cellCount = remember {
                                    MySettingsManager.getGridCount(this@MainActivity)
                                }
                                MainScreen2(
                                    isLinear = isLinear,
                                    cellCount = cellCount,
                                    onUpdate = {
                                        recreate()
                                    },
                                    cateList = {
                                        cateList = it
                                    },
                                    channel = currentRoute,
                                    onSplashFinish = viewModel::onSplashFinished,
                                    onSettingExit = { lastRoute ->
                                        onNavItemClick(lastRoute)
                                    }
                                )
                            }
                            ApplovinBanner()
                        }
                        AnimatedVisibility(
                            visible = isShowCate,
                            enter = slideInVertically(),
                            exit = slideOutVertically()
                        ) {
                            CategoriesCompose(
                                list = cateList,
                                onCateClick = {
                                    val channel =
                                        if (it.url.contains(MyConst.CmHost)) {
                                            Channel.ChannelMyanmar
                                        } else if (it.url.contains(MyConst.GcHost)) {
                                            Channel.GoldChannel
                                        } else if (it.url.contains(MyConst.MSubHost)) {
                                            Channel.MyanmarSubMovie
                                        } else {
                                            Channel.Unknown
                                        }
                                    Goto.onCateClick(this@MainActivity, channel, it)
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            )
                        }

                    }
                }

                BackHandler(enabled = drawerState.isOpen || isShowCate) {
                    if (drawerState.isOpen) {
                        scope.launch {
                            drawerState.close()
                        }
                    } else {
                        isShowCate = false
                    }

                }
            }

        }
    }
}