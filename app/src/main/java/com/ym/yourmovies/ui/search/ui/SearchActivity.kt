package com.ym.yourmovies.ui.search.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.ui.theme.YourMoviesTheme

class SearchActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MySettingsManager.upLauguage(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            YourMoviesTheme {
                MainSearch(
                    cellCount = MySettingsManager.getGridCount(this)
                )
            }

        }
    }

}