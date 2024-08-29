package com.ym.yourmovies.utils.components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.ym.yourmovies.utils.others.Js

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MyWebView(
    html: String,
    isAddJavaScript: Boolean = false,
    fontSize: Int = 14,
    onGenerate: ((link: String, title: String) -> Unit)? = null
) {
    AndroidView(factory = {
        FrameLayout(it)
    },
        update = {
            it.addView(
                WebView(it.context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)

                            if (isAddJavaScript) {
                                val function =
                                    " `androidClick('\${element.getAttribute('href')}','\${element.innerText}')`"
                                val script = """
                              var aEl = document.querySelectorAll("a"); 
        aEl.forEach((element, index) => { element.setAttribute("onclick", $function); 
        element.setAttribute('href', 'javascript:void(0);'); }); 
        function androidClick(link,title)
         { 
            Js.getLink(link,title); 
        }
                         """
                                view?.evaluateJavascript(script, null)
                            }
                        }
                    }
                    settings.javaScriptEnabled = true
                    settings.defaultFontSize = fontSize
                    settings.setGeolocationEnabled(false)
                    settings.setNeedInitialFocus(false)
                    if (isAddJavaScript) {
                        addJavascriptInterface(Js(onGenerate!!), "Js")
                    }
                    loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
                }
            )
        })

}