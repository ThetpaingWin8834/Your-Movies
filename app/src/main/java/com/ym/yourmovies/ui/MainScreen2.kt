package com.ym.yourmovies.ui


import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import com.ym.yourmovies.cm.home.ui.CmHomeContent
import com.ym.yourmovies.gc.home.ui.GcHomeContent
import com.ym.yourmovies.msub.home.ui.MSubHomeContent
import com.ym.yourmovies.ui.main.ChannelRoute
import com.ym.yourmovies.ui.main.SplashScreen
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.ui.settings.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen2(
    channel:ChannelRoute,
    isLinear: Boolean,
    cellCount: Int,
    onUpdate:() -> Unit,
    onSplashFinish:()->Unit,
    onSettingExit:(ChannelRoute)->Unit,
    cateList :(List<CategoryItem>)->Unit
) {
    var lastRoute = remember {
        ChannelRoute.Splash
    }
    AnimatedContent(
        targetState = channel,
        transitionSpec = {
            fadeIn(animationSpec = tween(1000))+ scaleIn() with fadeOut(animationSpec = tween(1000))+ scaleOut()
        }
        ) { currChannel->
        when(currChannel){
            ChannelRoute.ChannelMyanmar -> {
                CmHomeContent(
                    isLinear = isLinear,
                    cellCount = cellCount,
                    cateItems =  cateList
                )
                lastRoute = currChannel
            }
            ChannelRoute.GoldChannel -> {
                GcHomeContent(
                    isLinear = isLinear,
                    cellCount = cellCount,
                    cateItems =  cateList
                )
                lastRoute = currChannel
            }
            ChannelRoute.MyanmarSubtitles -> {
                MSubHomeContent(
                    isLinear = isLinear,
                    cellCount = cellCount,
                    cateItems =  cateList
                )
                lastRoute = currChannel
            }
            ChannelRoute.Splash -> SplashScreen( onSplashFinish = onSplashFinish)
            ChannelRoute.Settings-> SettingsScreen (onUpdate, onback = {
                onSettingExit(lastRoute)
            })
        }
    }


}