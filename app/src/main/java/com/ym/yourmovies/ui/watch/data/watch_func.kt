package com.ym.yourmovies.ui.watch.data

import com.ym.yourmovies.download.utils.LinkGenerator
import com.ym.yourmovies.download.utils.USER_AGENT_CHROME
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.utils.others.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup

enum class WatchServer {
    MegaUp, YoteShinDrive, Gdrive, MediaFire, Unknown
}


suspend fun directLinkFromChannel(
    channel: Channel,
    link: String,
    watchServer: WatchServer
): Flow<Response<String>> {
    return when (channel) {
        Channel.ChannelMyanmar -> {
            LinkGenerator.directLinkFormLeet(link)
        }
        Channel.GoldChannel -> {
            getDirectLinkFromRedirectForGc(link, watchServer)
        }
        else -> flow { emit(Response.Error(Exception("Unknown error occur"))) }
    }
}

private suspend fun getDirectLinkFromRedirectForGc(
    orginalLink: String,
    watchServer: WatchServer
): Flow<Response<String>> {
    return flow {
        try {
            emit(Response.Loading())
            val response = Jsoup.connect(orginalLink)
                .userAgent(USER_AGENT_CHROME)
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .execute()
            var redirectedLink = response.url().toExternalForm()
            if (redirectedLink.contains("mobilevsnews.com")) {
                val document = response.parse()
                redirectedLink = document.select("a.redirect").attr("href")

            }
            if (redirectedLink.isNullOrEmpty()) {
                emit(Response.Error(LinkNotSupportedException()))
            } else if (watchServer == WatchServer.Gdrive) {
                emit(Response.Success(redirectedLink))
            } else {
                val document = Jsoup.connect(MyConst.LeetUrl)
                    .data("link", redirectedLink)
                    .ignoreContentType(true)
                    .post()
                val obj = JSONObject(document.body().text())
                val array = JSONArray(obj.getString("finalurl"))
                val generatedLink = array[0].toString().replace("\\\\", "")
                if (generatedLink.isEmpty() || generatedLink.contains(
                        "not a valid link",
                        ignoreCase = true
                    )
                ) {
                    emit(Response.Error(LinkNotSupportedException()))
                } else {
                    emit(Response.Success(generatedLink))
                }
            }
        } catch (exception: JSONException) {
            emit(
                Response.Error(Exception("Links not supported .Try another!"))
            )
        } catch (e: Exception) {
            emit(
                Response.Error(e)
            )
        }
    }
}

fun watchServerOf(url: String): WatchServer {
    return if (url.isContainGdrive()) {
        WatchServer.Gdrive
    } else if (url.isContainMediaFire()) {
        WatchServer.MediaFire
    } else if (url.isContainMegaUp()) {
        WatchServer.MegaUp
    } else if (url.isContainYoteShin()) {
        WatchServer.YoteShinDrive
    } else {
        WatchServer.Unknown
    }
}