package com.ym.yourmovies.utils.components.ads

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView

@Composable
fun rememberNativeAdsLoader(
    nativeId:String,
    context: Context = LocalContext.current
):MutableState<MaxNativeAdView?>
{
    val nativeAdLoader = remember {
        MaxNativeAdLoader(nativeId,context)
    }
    var maxAd = remember <MaxAd?> {
        null
    }
    val adView = remember {
        mutableStateOf<MaxNativeAdView?>(null)
    }
    LaunchedEffect(key1 = nativeId){
        nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(view: MaxNativeAdView?, ad: MaxAd?) {
                if (maxAd != null) {
                    nativeAdLoader.destroy(maxAd)
                }
                // Save ad for cleanup.
                maxAd = ad
                adView.value = view
            }
        })
        nativeAdLoader.loadAd()
    }
    return  adView
    }

@Composable
fun NativeAdView(
    modifier: Modifier = Modifier,
    maxAd: MaxNativeAdView,
) {
    AndroidView(
        factory = {
            maxAd
        },
        modifier = modifier.fillMaxWidth()
    )
}

fun LazyListScope.linearNativeAdsView(adsView:MaxNativeAdView?) {
    if (adsView!=null){
        item{
            NativeAdView(maxAd = adsView)
        }
    }
}
fun LazyGridScope.gridNativeAdsView(adsView:MaxNativeAdView?) {
    if (adsView!=null){
        item(span = { GridItemSpan(maxLineSpan) }){
            NativeAdView(maxAd = adsView)
        }
    }
}