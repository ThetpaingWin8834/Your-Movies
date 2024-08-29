package com.ym.yourmovies.utils.components.ads

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.ym.yourmovies.YmApp

@Composable
fun ApplovinBanner(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    var isLoaded by remember {
        mutableStateOf(false)
    }
    val height by animateDpAsState(targetValue = if (isLoaded) 50.dp else 0.dp)
    AndroidView(factory = {
        MaxAdView(YmApp.bannerId, context).apply {
            val adwidth = ViewGroup.LayoutParams.MATCH_PARENT
            val adheight = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams = FrameLayout.LayoutParams(adwidth, adheight)
        }
    }, update = {
        it.loadAd()
        it.startAutoRefresh()
        it.setListener(object : MaxAdViewAdListener {
            override fun onAdLoaded(p0: MaxAd?) {
                isLoaded = true
            }

            override fun onAdDisplayed(p0: MaxAd?) = Unit

            override fun onAdHidden(p0: MaxAd?) = Unit

            override fun onAdClicked(p0: MaxAd?) = Unit

            override fun onAdLoadFailed(p0: String?, p1: MaxError?) = Unit

            override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) = Unit

            override fun onAdExpanded(p0: MaxAd?) = Unit

            override fun onAdCollapsed(p0: MaxAd?) = Unit
        })
    },
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    )
}