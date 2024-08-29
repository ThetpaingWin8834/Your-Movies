package com.ym.yourmovies

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleEventObserver
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd
import com.applovin.sdk.AppLovinSdk
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.ui.settings.darkMode
import com.ym.yourmovies.ui.settings.lightMode


class YmApp : Application() {
   // private val gaid = "4f92e313-b88c-4c90-a376-1dba20d2d971"
    override fun onCreate() {
        super.onCreate()
        when (MySettingsManager.getTheme(this)) {
            darkMode -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }
            lightMode -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }
            else -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
        val sdk = AppLovinSdk.getInstance(this)
        sdk.initializeSdk {
            val appOpenAd = MaxAppOpenAd(appOpenId, applicationContext)
            appOpenAd.setListener(object : MaxAdListener {
                override fun onAdLoaded(p0: MaxAd?) {
                    appOpenAd.showAd()
                }

                override fun onAdDisplayed(p0: MaxAd?) = Unit

                override fun onAdHidden(p0: MaxAd?) = Unit

                override fun onAdClicked(p0: MaxAd?) = Unit

                override fun onAdLoadFailed(p0: String?, p1: MaxError?) {
                    appOpenAd.loadAd()
                }

                override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
                    appOpenAd.loadAd()
                }
            })
            // appOpenAd.loadAd()
        }
//        sdk.mediationProvider
//
//        sdk.settings.testDeviceAdvertisingIds = arrayListOf(gaid)
    }


    companion object {
        const val interId = "806e6b2ca2e2b1e5"
        const val bannerId = "beee6a7cb8793ae2"
        const val appOpenId = "62705c6f7c7f5c8d"
        const val nativeId = "199c53db8d094827"
        const val nativeSmallId = "d92302cbfe7068c8"
    }
}