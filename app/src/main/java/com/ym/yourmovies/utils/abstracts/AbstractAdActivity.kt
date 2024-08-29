package com.ym.yourmovies.utils.abstracts

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.ym.yourmovies.R
import com.ym.yourmovies.YmApp
import com.ym.yourmovies.ui.settings.MySettingsManager
import java.util.concurrent.TimeUnit
import kotlin.math.pow

fun interface OnAdsHide{
    fun onHide(isFailed:Boolean)
}

abstract class AbstractAdActivity :AppCompatActivity(),MaxAdListener {
    private lateinit var interstitialAd: MaxInterstitialAd
    private var retryAttempt = 0.0
    private var isAdShown = false

    private var onAdsHide: OnAdsHide?=null
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MySettingsManager.upLauguage(newBase))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        interstitialAd = MaxInterstitialAd( YmApp.interId, this )
        interstitialAd.setListener( this )

        // Load the first ad
        interstitialAd.loadAd()
    }

    override fun onAdLoaded(maxAd: MaxAd)
    {
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'

        // Reset retry attempt
        retryAttempt = 0.0
//        if (!isAdShown){
//            interstitialAd.showAd()
//        }
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
        onAdsHide?.onHide(true)

    }
    override fun onAdDisplayed(maxAd: MaxAd) {}

    override fun onAdClicked(maxAd: MaxAd)=Unit
    override fun onAdHidden(maxAd: MaxAd)
    {
        // Interstitial ad is hidden. Pre-load the next ad
        isAdShown = true
        interstitialAd.loadAd()
        onAdsHide?.onHide(false)

    }
    fun showAd(onAdsHide: OnAdsHide){
        interstitialAd.showAd()
        Toast.makeText(this, R.string.showing_ads,Toast.LENGTH_SHORT).show()
        this.onAdsHide = onAdsHide
    }
    fun showAdIfNotShown(){
        if (!isAdShown){
            interstitialAd.showAd()
        }
    }
    fun isAdReady() = interstitialAd.isReady
    fun isAdShown()=isAdShown
//    fun registerOnHide(onAdsHide: OnAdsHide){
//        this.onAdsHide = onAdsHide
//    }
    fun unRegisterAdsHide(){
        this.onAdsHide=null
    }
}