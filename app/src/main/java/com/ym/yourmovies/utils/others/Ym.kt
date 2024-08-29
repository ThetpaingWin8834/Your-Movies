package com.ym.yourmovies.utils.others

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.applovin.adview.AppLovinInterstitialAd
import com.applovin.mediation.ads.MaxInterstitialAd
import com.ym.yourmovies.R

object Ym {
    fun copyLink(context: Context, link: String) {
        val cpMan = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText("Link ", link)
        cpMan.setPrimaryClip(data)
        Toast.makeText(context, R.string.link_ready_to_paste, Toast.LENGTH_SHORT).show()
    }

    fun download(context: Context, link: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            ContextCompat.startActivity(context, intent, null)
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage ?: "Unknown error occurs", Toast.LENGTH_LONG)
                .show()
        }

    }

    fun launchYoteShinDriveApp(context: Context, intentUri: String) {
        try {
            val intent = Intent.parseUri(intentUri, Intent.URI_INTENT_SCHEME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(context, intent, null)
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage ?: "Unknown error occurs", Toast.LENGTH_LONG)
                .show()
        }

    }
}