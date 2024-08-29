package com.ym.yourmovies.ui.settings

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.ym.yourmovies.ui.main.ChannelRoute
import com.ym.yourmovies.utils.others.Route

class SettingsViewModel(
    val app: Application
) : AndroidViewModel(app) {
    var currentTheme by mutableStateOf(getDefaultTheme())
        private set

    fun setCurrTheme(theme: String) {
        if (currentTheme != theme) currentTheme = theme
    }

    var currentLang by mutableStateOf(getDefaultLanguage())
        private set

    var defaultChannel by mutableStateOf(MySettingsManager.getDefaultChannel(app))
        private set

    fun changeDefaultChannel(channelRoute: String) {
        val channel = ChannelRoute.valueOf(channelRoute)
        if (defaultChannel != channel) {
            defaultChannel = channel
            MySettingsManager.setDefaultChannel(app,channel)
        }
    }

    fun setCurrLang(lang: String) {
        if (currentLang != lang) currentLang = lang
    }

    val listOfLang by lazy {
        listOf(Myanmar, English)
    }
    val listOfTheme by lazy {
        listOf(lightMode, darkMode, autoMode)
    }
    val listOfChannel by lazy {
        listOf(ChannelRoute.ChannelMyanmar.name,ChannelRoute.GoldChannel.name,ChannelRoute.MyanmarSubtitles.name)
    }
    val listOfDm by lazy {
        listOf(dm_1dm, dm_browser, dm_ask)
    }
    val listOfGirdCount by lazy {
        listOf("2","3","4")
    }
    var defDm by mutableStateOf(getDefaultDownloadManager())
    fun onDownloadmanagerChange (dm:String){
        if (defDm!=dm){
            defDm = dm
        }
    }

    var currentGridCount by mutableStateOf(getDefaultGridCount())
    private set

    fun changeGridCount(gridCount:Int){
        if (currentGridCount!=gridCount)currentGridCount = gridCount
    }

    var currentIsLinear by mutableStateOf(getIsLnear())
    fun changeIsLinearTo(isLinear:Boolean){
        if (currentIsLinear!=isLinear)currentIsLinear = isLinear
    }
    var currUseDynamic by mutableStateOf(getUseDynamic())
    fun changeUseDynamicTo(use:Boolean){
        if (currUseDynamic!=use)currUseDynamic = use
    }

    val listOfSearchAndRecomm by lazy {
        listOfChannel+ InAll
    }
    var currSearchIn by mutableStateOf(getDefSearchInChannel())
    var currRecommIn by mutableStateOf(getDefRecommChannel())

    fun setCurrSearchChannel (channel: String){
        if (currSearchIn!=channel)currSearchIn = channel
    }
    fun setCurrRecommChannel (channel: String){
        if (currRecommIn!=channel)currRecommIn = channel
    }

    fun getDefSearchInChannel () = MySettingsManager.getDefaultSearchIn(app)
    fun getDefRecommChannel () = MySettingsManager.getDefaultRecomm(app)
    fun getIsLnear() = MySettingsManager.getIsLinear(app)
    fun getUseDynamic() = MySettingsManager.getUseDynamic(app)
    fun getDefaultTheme() = MySettingsManager.getTheme(app)

    fun getDefaultLanguage() = MySettingsManager.getLang(app)

    fun getDefaultGridCount() = MySettingsManager.getGridCount(app)
    fun getDefaultDownloadManager() =MySettingsManager.downloadWith(context = app)

}