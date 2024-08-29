package com.ym.yourmovies.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.ym.yourmovies.ui.main.ChannelRoute
import com.ym.yourmovies.utils.others.Route
import java.util.*

private const val sp_name="ym_settings"
private const val defThemeMode="ym_thememode"
 const val lightMode="Light"
 const val darkMode="Dark"
 const val autoMode="Auto"

private const val defLanguage = "ym_lang"
const val Myanmar = "Myanmar"
const val English = "English"

private const val defaultChannel = "ym_def_channel"

private const val defIsLinear = "ym_islinear"

private const val defGridCount = "ym_gridcount"

private const val defDm ="ym_dm"
const val dm_browser="Browser"
const val dm_1dm="1 DM"
const val dm_ask="Always ask"

private const val defUseDynamic = "ym_use_dynamic"
private const val defSearchChannels = "ym_search_channel"
private const val defRecommChannels = "ym_recomm_channel"
//const val InCm = "Channel Myanmar"
//const val InGc= "Gold Channel"
//const val InMsub = "Myanmar Subtitles"
const val InAll = "All"
const val NONE = "None"

object MySettingsManager {
     fun getSharedPref(context: Context): SharedPreferences = context.getSharedPreferences(sp_name,Context.MODE_PRIVATE)

   fun getTheme(context: Context):String{
       val sp = getSharedPref(context.applicationContext)
       return sp.getString(defThemeMode, autoMode)?: autoMode
   }
    fun setTheme(context: Context,theme:String){
        val sp = getSharedPref(context.applicationContext)
        val editor = sp.edit()
        editor.putString(defThemeMode,theme)
        editor.apply()
    }
    fun setLang(context: Context,lang:String){
        val sp = getSharedPref(context.applicationContext)
        val editor = sp.edit()
        editor.putString(defLanguage,lang)
        editor.apply()
    }
    fun getLang(context: Context):String{
       val sp = getSharedPref(context.applicationContext)
       return sp.getString(defLanguage, English)?: English
   }
    fun getDefaultChannel(context: Context):ChannelRoute{
        val sp = getSharedPref(context.applicationContext)
        return ChannelRoute.valueOf(sp.getString(defaultChannel, ChannelRoute.ChannelMyanmar.name)?: ChannelRoute.ChannelMyanmar.name)
    }
    fun setDefaultChannel(context: Context,channelRoute: ChannelRoute){
        val sp = getSharedPref(context.applicationContext)
        val editor = sp.edit()
        editor.putString(defaultChannel,channelRoute.name)
        editor.apply()
    }
    fun getIsLinear(context: Context):Boolean{
        val sp = getSharedPref(context.applicationContext)
        return sp.getBoolean(defIsLinear, false)
    }
    fun setIsLinearTo(context: Context,isLinear:Boolean){
        val sp = getSharedPref(context.applicationContext)
        val editor = sp.edit()
        editor.putBoolean(defIsLinear,isLinear)
        editor.apply()
    }
    fun getUseDynamic(context: Context):Boolean{
        val sp = getSharedPref(context.applicationContext)
        return sp.getBoolean(defUseDynamic, true)
    }
    fun setUseDynamic(context: Context,useDynamic:Boolean){
        val sp = getSharedPref(context.applicationContext)
        val editor = sp.edit()
        editor.putBoolean(defUseDynamic,useDynamic)
        editor.apply()
    }
    fun getGridCount(context: Context):Int{
        val sp = getSharedPref(context.applicationContext)
        return sp.getInt(defGridCount,3)
    }
    fun setGridCount(context: Context,gridCount:Int){
        val sp = getSharedPref(context.applicationContext)
        val editor = sp.edit()
        editor.putInt(defGridCount,gridCount)
        editor.apply()
    }
    fun downloadWith(context: Context) :String{
        val sp = getSharedPref(context)
        return sp.getString(defDm, dm_ask)?: dm_ask
    }
    fun setDownloadWith(context: Context,dm:String){
        val sp = getSharedPref(context.applicationContext)
        val editor = sp.edit()
        editor.putString(defDm,dm)
        editor.apply()
    }

    fun getDefaultSearchIn(context: Context):String{
        val sp = getSharedPref(context.applicationContext)
        return sp.getString(defSearchChannels, InAll)?: InAll
    }
    fun setDefaultSearchIn(context: Context,channel:String){
        val sp = getSharedPref(context.applicationContext)
        val editor = sp.edit()
        editor.putString(defSearchChannels,channel)
        editor.apply()
    }
    fun getDefaultRecomm(context: Context):String{
        val sp = getSharedPref(context.applicationContext)
        return sp.getString(defRecommChannels, InAll)?: InAll
    }
    fun setDefaultRecomm(context: Context,channel:String){
        val sp = getSharedPref(context.applicationContext)
        val editor = sp.edit()
        editor.putString(defRecommChannels,channel)
        editor.apply()
    }
    fun upLauguage(c: Context): Context? {
        val lan = when(getLang(c)){
            Myanmar->"my"
            else->"en"
        }
        val loc = Locale(lan)
        Locale.setDefault(loc)
        val configuration = Configuration(c.resources.configuration)
        configuration.setLocale(loc)
        return c.createConfigurationContext(configuration)
    }
}