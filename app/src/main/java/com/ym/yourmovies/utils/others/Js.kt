package com.ym.yourmovies.utils.others

import android.webkit.JavascriptInterface

class Js(
   val onGenerate:(link:String,title:String)->Unit
) {
    @JavascriptInterface
    fun getLink(link: String,title: String) {
       onGenerate(link,title)
    }


}