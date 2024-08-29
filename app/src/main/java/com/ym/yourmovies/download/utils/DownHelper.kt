package com.ym.yourmovies.download.utils

import android.app.Activity
import android.content.Context
import com.ym.yourmovies.ui.settings.dm_1dm
import com.ym.yourmovies.ui.settings.dm_ask
import com.ym.yourmovies.ui.settings.dm_browser
import com.ym.yourmovies.utils.others.Util1DM
import com.ym.yourmovies.utils.others.Ym

object DownHelper {
   inline fun downWith(which: String,activity: Activity,url:String, onAsk: () -> Unit) {
        when (which) {
            dm_ask -> {
                onAsk()
            }
            dm_1dm -> {
                Util1DM.downloadFile(activity,url,false,true)
            }
            dm_browser -> {
                Ym.download(activity,url)
            }
        }
    }
}