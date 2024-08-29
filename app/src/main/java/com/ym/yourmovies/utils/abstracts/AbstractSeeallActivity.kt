package com.ym.yourmovies.utils.abstracts

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.ym.yourmovies.R
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.ui.settings.darkMode
import com.ym.yourmovies.ui.settings.lightMode
import com.ym.yourmovies.ui.theme.YourMoviesTheme
import com.ym.yourmovies.utils.adapters.AllLoadStateAdapter
import com.ym.yourmovies.utils.components.LoadingAndError
import com.ym.yourmovies.utils.models.PageAndQuery
import com.ym.yourmovies.utils.models.SeeAllModel
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.MyConst

abstract class AbstractSeeallActivity<Movie : Any> : AppCompatActivity() {
    private val tvCurrPage by lazy {
        findViewById<TextView>(R.id.tvSeeallPage)
    }
    private val recy by lazy {
        findViewById<RecyclerView>(R.id.recySeeAll)
    }
    private val compose by lazy {
        findViewById<ComposeView>(R.id.composeSeeAll)
    }
    private var dia: AlertDialog? = null
    private var et: TextInputEditText? = null
    abstract val viewModel: AbstractSeeAllViewModel<Movie>
     val isLinear by lazy {
        MySettingsManager.getIsLinear(this)
    }
    private val spanCount by lazy {
        MySettingsManager.getGridCount(this)
    }
    private var _ada: PagingDataAdapter<Movie, *>? = null
    private val adapter get() = _ada!!
    abstract fun getPagingAdapter(): PagingDataAdapter<Movie, *>
    private val pageOrQuery by lazy {
        val _pageOrQuery = intent.getStringExtra(MyConst.queyOrUrl) ?: MyConst.CmHost
        if (isFromSearch || _pageOrQuery.endsWith("/")) _pageOrQuery else "$_pageOrQuery/"
    }
    private val isFromSearch by lazy {
        intent.getBooleanExtra(MyConst.isFromSearch, false)
    }
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MySettingsManager.upLauguage(newBase))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
//        when(MySettingsManager.getTheme(this)){
//            lightMode->{
//                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
//            }
//            darkMode->{
//                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
//
//            }
//            else->{
//                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//
//            }
//        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seeall)
        setSupportActionBar(findViewById(R.id.tbSeeAll))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title = intent.getStringExtra(MyConst.title)
        if (!title.isNullOrEmpty()) {
            supportActionBar?.title = title
        }
        _ada = getPagingAdapter()

        recy.setHasFixedSize(true)
        recy.layoutManager = if (isLinear) {
            LinearLayoutManager(this)
        } else {
            GridLayoutManager(this, spanCount)
        }


        recy.adapter = adapter.withLoadStateHeaderAndFooter(
            header = AllLoadStateAdapter {
                adapter.retry()
            },
            footer = AllLoadStateAdapter {
                adapter.retry()
            }
        )
        var isLoading by mutableStateOf(false)
        var error: Throwable? by mutableStateOf(null)
        compose.setContent {
            YourMoviesTheme {
                LoadingAndError(isLoading = isLoading, error = error, onErrorClick = adapter::retry)
            }
            val shouldRecyShow by remember {
                derivedStateOf {
                    !isLoading && error == null
                }
            }
            LaunchedEffect(key1 = shouldRecyShow) {
                recy.isVisible = shouldRecyShow
            }
        }
        viewModel.pager.observe(this) {
            adapter.submitData(this.lifecycle, it)
        }
        viewModel.currPage.observe(this) {
            tvCurrPage.text = "$it"
        }

        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    isLoading = true
                    if (error != null) error = null
                }
                is LoadState.NotLoading -> {
                    isLoading = false
                }
                is LoadState.Error -> {
                    isLoading = false
                    error = (it.refresh as LoadState.Error).error

                }
            }
        }
        viewModel.pageAndQuery.value = SeeAllModel(
            isFromSearch = isFromSearch,
            pageAndQuery = PageAndQuery(page = 1, pageOrQuery)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.see_all_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
            R.id.goto_page -> {
                showDia()
            }
            R.id.goto_search->{
                Goto.goSearch(this)
            }
            R.id.goto_wl->{
                Goto.goWatchLater(this)
            }

        }
        return true
    }

    private fun showDia() {
        if (dia == null) {
            dia = AlertDialog.Builder(this)
                .setView(R.layout.custom_page_dialog)
                .setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Go") { _, _ ->
                    if (et == null) et = dia!!.findViewById(R.id.diaEt)
                    val num = et!!.text.toString().trim().toIntOrNull()
                    if (num != null) {
                        viewModel.pageAndQuery.value = SeeAllModel(
                            isFromSearch = isFromSearch,
                            pageAndQuery = PageAndQuery(page = num, pageOrQuery)
                        )
                        recy.scrollToPosition(0)
                        dia!!.dismiss()
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()
        }
        dia!!.show()
    }
}