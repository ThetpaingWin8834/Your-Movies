package com.ym.yourmovies.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ym.yourmovies.ui.settings.MySettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    val app: Application
) : AndroidViewModel(app) {

    private val _currRoute = MutableStateFlow("splash")
    val currRoute = _currRoute.asStateFlow()

    val currChannel = MutableStateFlow(ChannelRoute.Splash)
    fun onCurrentChannelChange(channelRoute: ChannelRoute) {
        if (currChannel.value != channelRoute) {
            currChannel.value = channelRoute
        }
    }

    fun onSplashFinished() {
        currChannel.value = MySettingsManager.getDefaultChannel(app)
    }

    fun setCurrentChannelTo(route: String?) {
        if (route != null && _currRoute.value != route) {
            _currRoute.value = route
        }
    }
}

enum class ChannelRoute {
    Splash, ChannelMyanmar, GoldChannel, MyanmarSubtitles, Settings
}