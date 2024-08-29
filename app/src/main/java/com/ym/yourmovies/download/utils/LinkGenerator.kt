package com.ym.yourmovies.download.utils

import android.webkit.URLUtil
import com.google.gson.Gson
import com.ym.yourmovies.msub.model.YoutubeDownloadModel
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.utils.others.LinkNotSupportedException
import com.ym.yourmovies.utils.others.MyConst
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup

const val USER_AGENT_CHROME =
    "Mozilla/5.0 (Linux; Android 10; SM-G960F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.81 Mobile Safari/537.36"
const val USER_AGENT_FIREFOX = "Mozilla/5.0 (Android 11; Mobile; rv:81.0) Gecko/81.0 Firefox/81.0"


object LinkGenerator {
    suspend fun directLinkFormLeet(originalLink: String): Flow<Response<String>> {
        val isMegaUp = originalLink.contains("megaup.net")
        return flow {
            emit(Response.Loading())
            try {
                val document = Jsoup.connect(MyConst.LeetUrl)
                    .data("link", originalLink)
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
                    if (isMegaUp){
                        val originalName = URLUtil.guessFileName(originalLink,null,null)
                        val driectName = URLUtil.guessFileName(generatedLink,null,null)
                        if (originalName.contains(driectName)){
                            emit(Response.Success(generatedLink))
                        }else{
                            emit(Response.Error(LinkNotSupportedException()))
                        }
                    }else{
                        emit(Response.Success(generatedLink))
                    }

                }
            }
            catch (exception:JSONException){
                emit(
                    Response.Error(Exception("Links not supported .Try another!"))
                )
            }
            catch (e: Exception) {
                emit(
                    Response.Error(e)
                )
            }
        }
    }

    suspend fun openYoteShinDriveAppFromLink(
        link: String,
        userAgent: String = USER_AGENT_CHROME
    ): Flow<Response<String>> {
        return flow {
            try {
                emit(Response.Loading())

                val document = Jsoup.connect(link).userAgent(userAgent).get()
                val intentText = document.getElementById("countdown-btn")?.attr("href")
                if (userAgent == USER_AGENT_FIREFOX){
                    emit(Response.Error(Exception("Unknown error occurs")))
                }else if (intentText.isNullOrEmpty()) {
                    openYoteShinDriveAppFromLink(link = link, userAgent = USER_AGENT_FIREFOX)
                } else {
                    emit(Response.Success(intentText))
                }


            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    }

    suspend fun getRedirectLinkFrom(url: String): Flow<Response<String>> {
        return flow {
            try {
                emit(Response.Loading())
                val response = Jsoup.connect(url)
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
                } else {
                    emit(Response.Success(redirectedLink))
                }

            } catch (e: Exception) {
                if (e is HttpStatusException){
                    if (e.statusCode==429){
                        emit(Response.Success(e.url))
                    }else{
                        emit(Response.Error(e))

                    }
                }else{
                    emit(Response.Error(e))
                }
            }
        }
    }

    suspend fun generateYtLinks(url: String):Flow<Response<YoutubeDownloadModel>> = flow {
        try {
            emit(Response.Loading())
            val apiUrl = "https://ssyoutube.com/api/convert"
            val firstDocument = Jsoup.connect(MyConst.ssTube).get()
            val token = firstDocument.select("input[name=_token]").attr("value")
            val requestBody = """
                {
                  "_token":"$token",
                  "url":"$url"
                }
            """.trimIndent()
            val response = Jsoup.connect(apiUrl)
                .header("Content-Type", "application/json,text/plain,*/*")
                .ignoreContentType(true)
                .requestBody(requestBody)
                .post()
            val youtubeDownloadModel = Gson().fromJson(response.body().text(),YoutubeDownloadModel::class.java)


            val urls = youtubeDownloadModel.url.filter {
                !it.audio && !it.no_audio
            }
            emit(Response.Success(youtubeDownloadModel.copy(url = urls)))
        }catch (e:Exception){
            emit(Response.Error(e))
        }
    }

}