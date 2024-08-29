package com.ym.yourmovies.gc.seeall.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.ym.yourmovies.R
import com.ym.yourmovies.gc.seeall.adapters.GcSeeAllLinearRecyAdapter
import com.ym.yourmovies.gc.seeall.adapters.GcSeeAllRecyAdapter
import com.ym.yourmovies.gc.seeall.data.GcSeeAllViewmodel
import com.ym.yourmovies.ui.theme.YourMoviesTheme
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.utils.adapters.AllLoadStateAdapter
import com.ym.yourmovies.utils.components.LoadingAndError
import com.ym.yourmovies.utils.models.PageAndQuery
import com.ym.yourmovies.utils.models.SeeAllModel

class GcSeeAllActivity : AppCompatActivity() {
    private val tvCurrPage by lazy {
        findViewById<TextView>(R.id.tvSeeallPage)
    }
    private val recy by lazy {
        findViewById<RecyclerView>(R.id.recySeeAll)
    }
    private val compose by lazy {
        findViewById<ComposeView>(R.id.composeSeeAll)
    }
    private var dia : AlertDialog?=null
    private var et : TextInputEditText?=null
    private val pageOrQuery by lazy {
        val _pageOrQuery = intent.getStringExtra(MyConst.queyOrUrl)?: MyConst.GcHost
        if(isFromSearch || _pageOrQuery.endsWith("/"))_pageOrQuery else "$_pageOrQuery/"
    }
    private val isLinear by lazy {
        true
    }
    private val spanCount by lazy {
        3
    }
    private val layoutManager by lazy {
        if (isLinear){
            LinearLayoutManager(this)
        }else{
            GridLayoutManager(this,spanCount)
        }
    }
    private val linearAdapter by lazy {
        GcSeeAllLinearRecyAdapter()
    }
    private val gridAdapter by lazy {
        GcSeeAllRecyAdapter()
    }
    private val adapter by lazy {
        if(isLinear){
           linearAdapter
        }else{
            gridAdapter
        }
    }
    private val isFromSearch by lazy {
        intent.getBooleanExtra(MyConst.isFromSearch,false)
    }
    private val viewModel by viewModels<GcSeeAllViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window,false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seeall)
        setSupportActionBar(findViewById(R.id.tbSeeAll))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title = intent.getStringExtra(MyConst.title)
        if (!title.isNullOrEmpty()){
            supportActionBar?.title = title
        }
        recy.setHasFixedSize(true)
        recy.layoutManager=layoutManager


        recy.adapter=adapter.withLoadStateHeaderAndFooter(
            header = AllLoadStateAdapter{
                adapter.retry()
            },
            footer = AllLoadStateAdapter{
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
                    !isLoading && error==null
                }
            }
            LaunchedEffect(key1 = shouldRecyShow) {
                recy.isVisible = shouldRecyShow
            }
        }
        if (isLinear){
            viewModel.getLinearPager().observe(this){
                linearAdapter.submitData(this.lifecycle,it)
            }
        }else{
            viewModel.getPager().observe(this){
                gridAdapter.submitData(this.lifecycle,it)
            }
        }

        viewModel.currPage.observe(this){
            tvCurrPage.text="$it"
        }

        adapter.addLoadStateListener {
            when(it.refresh){
                is LoadState.Loading->  {
                    isLoading =true
                    if (error!=null)error=null
                }
                is LoadState.NotLoading->{
                    isLoading = false
                }
                is LoadState.Error ->{
                    isLoading=false
                    error =   (it.refresh as LoadState.Error).error
                }
            }
        }
        viewModel.pageAndQuery.value = SeeAllModel(
            isFromSearch = isFromSearch,
            pageAndQuery = PageAndQuery(page = if (isFromSearch)0 else 1 , pageOrQuery)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.see_all_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                onBackPressedDispatcher.onBackPressed()
            }
            R.id.goto_page->{
                showDia()
            }

        }
        return true
    }
    private  fun showDia(){
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