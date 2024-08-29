package com.ym.yourmovies.utils.deligate

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.ym.yourmovies.YmApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.pow

interface LoadAds{
    fun load(activity:Activity)
    fun showAd()
    fun isAdShown():Boolean
}

class InterstitialDeligation :LoadAds,MaxAdListener{
    private lateinit var interstitialAd: MaxInterstitialAd
    private var retryAttempt = 0.0
    private var isAdShown = false
    override fun showAd() {
        if (interstitialAd.isReady){
            interstitialAd.showAd()
        }
    }

    override fun isAdShown(): Boolean =isAdShown
    override fun load(activity: Activity) {
        interstitialAd = MaxInterstitialAd( YmApp.interId, activity )
        interstitialAd.setListener( this )

        // Load the first ad
        interstitialAd.loadAd()
    }
    override fun onAdLoaded(maxAd: MaxAd)
    {
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'

        // Reset retry attempt
        retryAttempt = 0.0
        if (!isAdShown){
            interstitialAd.showAd()
        }
    }

    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?)
    {
        // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

        retryAttempt++
        val delayMillis = TimeUnit.SECONDS.toMillis( 2.0.pow(6.0.coerceAtMost(retryAttempt)).toLong() )
        Handler(Looper.getMainLooper()).postDelayed(interstitialAd::loadAd,delayMillis)
    }

    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?)
    {
        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        interstitialAd.loadAd()

    }

    override fun onAdDisplayed(maxAd: MaxAd) {}

    override fun onAdClicked(maxAd: MaxAd) {}
    override fun onAdHidden(maxAd: MaxAd)
    {
        // Interstitial ad is hidden. Pre-load the next ad
        isAdShown = true
        interstitialAd.loadAd()
    }
}