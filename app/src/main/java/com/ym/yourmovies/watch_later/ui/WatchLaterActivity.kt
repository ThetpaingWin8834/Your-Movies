package com.ym.yourmovies.watch_later.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.ym.yourmovies.ui.theme.YourMoviesTheme
import com.ym.yourmovies.utils.abstracts.AbstractAdActivity

class WatchLaterActivity : AbstractAdActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window,false)
        super.onCreate(savedInstanceState)
        setContent {
            showAdIfNotShown()
            YourMoviesTheme {
                WatchLaterScreen(spanCount = 3, backPressedDispatcher = onBackPressedDispatcher)
            }
        }
    }
}