package com.ym.yourmovies.ui.search.ui

import android.content.Context
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ym.yourmovies.ui.search.random.RandomRecommandScreen
import com.ym.yourmovies.ui.search.result.SearchResultScreen
import com.ym.yourmovies.ui.search.result.SearchViewModel
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.ui.settings.NONE
import com.ym.yourmovies.utils.components.CategoriesCompose
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.MyConst

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainSearch(
    cellCount: Int,
    viewModel: SearchViewModel = viewModel(),
    focus: FocusManager = LocalFocusManager.current,
    backPressedDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    context: Context = LocalContext.current
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            SearchAppBar(
                viewModel = viewModel,
                focusRequester = focusRequester,
                onSearch = {
                    if (viewModel.query.isNotEmpty()) {
                        viewModel.searchMovies()
                        focus.clearFocus()
                    }
                },
                scrollBehavior = scrollBehavior,
                onBack = {
                    backPressedDispatcher?.onBackPressed()
                }
            )
        }
    ) { paddings ->

        var categoryList by remember {
            mutableStateOf<List<CategoryItem>?>(null)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            val searchScreen by viewModel.searchSceen.collectAsState()

            val isNone = remember {
                MySettingsManager.getDefaultRecomm(context) == NONE
            }
            if (isNone){

            }
            when (searchScreen) {
                SearchScreen.Search -> {
                    SearchResultScreen(viewModel = viewModel, cellCount = cellCount)
                }
                SearchScreen.Random -> {
                    RandomRecommandScreen(
                        cellCount = cellCount,
                        onCateMoreClick = {
                            categoryList = it
                        },
                        onRecentClick = {
                            viewModel.onQueryChange(it.query)
                            viewModel.searchMovies()
                            focus.clearFocus()
                        }
                    )
                }
            }
            AnimatedVisibility(
                visible = categoryList != null,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                CategoriesCompose(
                    list = categoryList ?: emptyList(), onCateClick = {
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
                        Goto.onCateClick(context, channel, it)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
            }
        }
        val isEnable by remember {
            derivedStateOf {
                if (categoryList != null) {
                    true
                } else if (viewModel.isFocus) {
                    viewModel.query.isNotEmpty()
                } else {
                    false
                }
            }
        }
        BackHandler(isEnable) {
            if (categoryList != null) {
                categoryList = null
            } else {
                focus.clearFocus()
            }
        }
        LaunchedEffect(key1 = true) {
            try {
                focusRequester.requestFocus()
            } catch (_: Exception) {
            }
        }
    }


}


enum class SearchScreen {
    Search, Random
}