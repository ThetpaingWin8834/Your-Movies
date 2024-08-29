package com.ym.yourmovies.ui.watch

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.ym.yourmovies.R
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.utils.others.MyConst

class VideoViewActivity : AppCompatActivity() {
    private val playerView by lazy {
        findViewById<StyledPlayerView>(R.id.player_view)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MySettingsManager.upLauguage(newBase))
    }

    private var exoPlayer: ExoPlayer? = null
    private var mDia: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)
        setSupportActionBar(findViewById(R.id.watch_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val movieTitle =
            intent.getStringExtra(MyConst.title) ?: System.currentTimeMillis().toString()

        supportActionBar?.title = movieTitle

        playerView.setControllerVisibilityListener(StyledPlayerView.ControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {
                showSystemBars()
            } else {
                hideSystemBars()
            }
        })
        playerView.setFullscreenButtonClickListener { full ->
            if (full) {
                enterLandScape()
                hideSystemBars()
            } else {
                enterPortrait()
                showSystemBars()
            }
        }
        playerView.setShowNextButton(false)
        playerView.setShowPreviousButton(false)
        playerView.hideController()
        initExoplayer()

    }

    private fun initExoplayer() {
        if (exoPlayer == null) {
            val videoUrl = intent.getStringExtra(MyConst.url)?.replace(" ", "%20")
            exoPlayer = ExoPlayer.Builder(this)
                .build()
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            exoPlayer!!.setMediaItem(mediaItem)
            exoPlayer!!.prepare()
            playerView.player = exoPlayer!!
            exoPlayer!!.playWhenReady = true
            playerView.hideController()

            exoPlayer!!.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    showDia(error.message ?: "Unknown error occurs")
                }
            })
        }
    }

    private fun showDia(message: String) {
        mDia = AlertDialog.Builder(this)
            .setNegativeButton(R.string.dismiss) { _, _ ->
                mDia?.dismiss()
            }
            .setTitle(R.string.sorry)
            .setMessage(message)
            .create()


    }

    override fun onResume() {
        super.onResume()
        playerView.onResume()
    }

    override fun onPause() {
        super.onPause()
        playerView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null
    }

    private fun enterLandScape() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun enterPortrait() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private fun hideSystemBars() {
        supportActionBar?.hide()
        val windowInsetsControllerCompat =
            WindowCompat.getInsetsController(window, window.decorView)
        //  WindowCompat.setDecorFitsSystemWindows(window, false)
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsControllerCompat.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE

//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
    }

    private fun showSystemBars() {
        supportActionBar?.show()
        val windowInsetsControllerCompat =
            WindowCompat.getInsetsController(window, window.decorView)
        //  WindowCompat.setDecorFitsSystemWindows(window, true)

        windowInsetsControllerCompat.show(WindowInsetsCompat.Type.systemBars())
//        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.watch_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
            R.id.watch_zoom -> {
                onZoom()
            }
        }
        return true
    }

    private fun onZoom() {
        when (playerView.resizeMode) {
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }
            AspectRatioFrameLayout.RESIZE_MODE_FIT -> {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            }
            AspectRatioFrameLayout.RESIZE_MODE_FILL -> {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
            }
            AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH -> {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
            }
            AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT -> {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }

        }
    }
}