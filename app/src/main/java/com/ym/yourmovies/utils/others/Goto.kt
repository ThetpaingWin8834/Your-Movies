package com.ym.yourmovies.utils.others

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ym.yourmovies.cm.details.ui.CmDetailsActivity
import com.ym.yourmovies.cm.seeall.ui.CmSeeAllActivity
import com.ym.yourmovies.gc.details.ui.GcDetailsActivity
import com.ym.yourmovies.gc.seeall.ui.GcSeeAllActivity
import com.ym.yourmovies.msub.details.MsubDetailsActivity
import com.ym.yourmovies.msub.seeall.ui.MSubSeeAllActivity
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.ui.search.ui.SearchActivity
import com.ym.yourmovies.ui.watch.VideoViewActivity
import com.ym.yourmovies.watch_later.ui.WatchLaterActivity

object Goto {
    fun cmDetails(context: Context, cmMovieBundle: Bundle){
        val intent = Intent(context, CmDetailsActivity::class.java)
        intent.putExtra(MyConst.movie,cmMovieBundle)
        context.startActivity(intent)
    }
    fun gcDetails(context: Context, gcMovieBundle: Bundle ){
        val intent = Intent(context, GcDetailsActivity::class.java)
        intent.putExtra(MyConst.movie,gcMovieBundle)
        context.startActivity(intent)
    }
    fun msubDetails(context: Context, msubMovieBundle: Bundle ){
        val intent = Intent(context, MsubDetailsActivity::class.java)
        intent.putExtra(MyConst.movie,msubMovieBundle)
        context.startActivity(intent)
    }
    fun cmSeeAll(context: Context, queryOrUrl : String,title:String,isFromSearch : Boolean = false){
        val intent = Intent(context, CmSeeAllActivity::class.java)
        intent.putExtra(MyConst.queyOrUrl,queryOrUrl.replace(" ","%20"))
        intent.putExtra(MyConst.title,title)
        intent.putExtra(MyConst.isFromSearch,isFromSearch)
        context.startActivity(intent)
    }
    fun gcSeeAll(context: Context, queryOrUrl : String,title:String ,isFromSearch : Boolean = false){
        val intent = Intent(context, GcSeeAllActivity::class.java)
        intent.putExtra(MyConst.queyOrUrl,queryOrUrl.replace(" ","%20"))
        intent.putExtra(MyConst.title,title)
        intent.putExtra(MyConst.isFromSearch,isFromSearch)
        context.startActivity(intent)
    }
    fun msubSeeAll(context: Context, queryOrUrl : String,title:String ,isFromSearch : Boolean = false){
        val intent = Intent(context, MSubSeeAllActivity::class.java)
        intent.putExtra(MyConst.queyOrUrl,queryOrUrl.replace(" ","%20"))
        intent.putExtra(MyConst.title,title)
        intent.putExtra(MyConst.isFromSearch,isFromSearch)
        context.startActivity(intent)
    }
    fun goSearch(context: Context){
        val intent = Intent(context, SearchActivity::class.java)
        context.startActivity(intent)
    }
    fun goWatchLater(context: Context){
        val intent = Intent(context, WatchLaterActivity::class.java)
        context.startActivity(intent)
    }
    fun onCateClick(context: Context, movieChannels: Channel, categoryItem: CategoryItem){
       when(movieChannels){
            Channel.ChannelMyanmar -> {
                cmSeeAll(context=context, queryOrUrl = categoryItem.url, title = categoryItem.title)
            }
            Channel.GoldChannel -> {
                gcSeeAll(context=context, queryOrUrl = categoryItem.url, title = categoryItem.title)

            }
            Channel.MyanmarSubMovie -> {
                msubSeeAll(context=context, queryOrUrl = categoryItem.url, title = categoryItem.title)
            }
            Channel.Unknown ->Unit
        }
    }

    fun goWatch(context: Context,title: String,url:String) {
        val intent = Intent(context, VideoViewActivity::class.java)
        intent.putExtra(MyConst.url,url)
        intent.putExtra(MyConst.title,title)
        context.startActivity(intent)
    }
}