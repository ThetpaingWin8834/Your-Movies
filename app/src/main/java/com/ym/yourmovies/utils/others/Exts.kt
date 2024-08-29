package com.ym.yourmovies.utils.others

import android.os.Bundle
import androidx.compose.runtime.MutableState
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.cm.seeall.model.GcMovieMeta
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.utils.models.Response
import com.ym.yourmovies.watch_later.model.WatchLaterItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


inline fun String.isEmptyOrEqual(value:String) = isNotEmpty()|| equals(value)

fun Element.toCmMovie(): CmMovie {
    return CmMovie(
        title = getElementsByTag("img").attr("alt"),
        description = getElementsByClass("ttx").text(),
        rating = getElementsByClass("imdb").text(),
        thumb = getElementsByTag("img").attr("src"),
        url = getElementsByTag("a").attr("href"),
      // date = getElementsByClass("year").text()
    )
}

fun CmMovie.toBundle(): Bundle {
    return Bundle().apply {
        putString("title", title)
        putString("rating", rating)
        putString("thumb", thumb)
        putString("url", url)
        putString("desc", description)
      //  putString("date", date)
    }
}

fun WatchLaterItem.toCmMovie(): CmMovie {
    return CmMovie(
        title = title,
        rating = rating,
        thumb = thumb,
        url = url,
        description = "",
    )
}
fun WatchLaterItem.toGcMovie(): GcMovie {
    return GcMovie(
        title = title,
        rating = rating,
        thumb = thumb,
        url = url,
        date = "",
    )
}
fun WatchLaterItem.toMusbMovie(): MSubMovie {
    return MSubMovie(
        title = title,
        rating = rating,
        thumb = thumb,
        url = url,
        date = "",
    )
}
fun Bundle.toCmItem(): CmMovie {
    return CmMovie(
        title = getString("title")!!,
        rating = getString("rating")!!,
        thumb = getString("thumb")!!,
        url = getString("url")!!,
        description = getString("desc")!!,
     //   date = getString("date")!!,
    )
}
fun GcMovie.toBundle(): Bundle {
    return Bundle().apply {
        putString("title", title)
        putString("rating", rating)
        putString("thumb", thumb)
        putString("url", url)
        putString("date", date)
    }
}
fun GcMovieMeta.toBundle(): Bundle {
    return Bundle().apply {
        putString("title", title)
        putString("rating", rating)
        putString("thumb", thumb)
        putString("url", url)
        putString("date", date)
    }
}
fun MSubMovie.toBundle(): Bundle {
    return Bundle().apply {
        putString("title", title)
        putString("rating", rating)
        putString("thumb", thumb)
        putString("url", url)
        putString("date", date)
    }
}
fun Bundle.toGcMovie(): GcMovie {
    return GcMovie(
        title = getString("title")!!,
        rating = getString("rating")!!,
        thumb = getString("thumb")!!,
        url = getString("url")!!,
        date = getString("date")!!,
    )
}
fun Bundle.toMsubMovie(): MSubMovie {
    return MSubMovie(
        title = getString("title")!!,
        rating = getString("rating")!!,
        thumb = getString("thumb")!!,
        url = getString("url")!!,
        date = getString("date")!!,
    )
}
fun Element.toGcMovie() : GcMovie = GcMovie(
    title = getElementsByTag("img").attr("alt"),
    date = select(".data span").text(),
    rating = getElementsByClass("rating").text(),
    thumb =  getElementsByTag("img").attr("src"),
    url =  getElementsByTag("a").attr("href")
)
fun Element.toGcMovieMeta() : GcMovieMeta = GcMovieMeta(
    title = getElementsByTag("img").attr("alt"),
    date = select(".data span").text(),
    rating ="IMDB:"+ getElementsByClass("rating").text(),
    thumb =  getElementsByTag("img").attr("src"),
    url =  getElementsByTag("a").attr("href"),
    duration = select(".metadata span:contains(min),span:contains(hr)").text(),
    views = select(".metadata span:contains(views)").text() ,
    desc = getElementsByClass("texto").text(),
    genres = select(".genres a[rel=tag]").text().replace(" "," | ")
)
fun Element.toMSubMovie () : MSubMovie=MSubMovie(
    title = getElementsByTag("img").attr("alt"),
    date = select(".data span").text(),
    rating = getElementsByClass("rating").text(),
    thumb = getElementsByTag("img").attr("src"),
    url = getElementsByTag("a").attr("href")
)

inline fun Elements.getElementWhen(condition: (Element) -> Boolean):Element?  {
    var ele : Element? = null
    for (el in this){
        if(condition.invoke(el)){
            ele = el
            break
        }
    }
     return  ele

}
suspend inline fun <T>Flow<Response<T>>.myCollect(
    crossinline onloading: () -> Unit ,
    crossinline onerror: (Exception) -> Unit,
    crossinline onsuccess: (T) -> Unit,
){
    collect{
        when(it){
            is Response.Error -> {
                onerror(it.exception)
            }
            is Response.Loading -> {
                onloading()
            }
            is Response.Success -> {
                withContext(Dispatchers.Main){
                    onsuccess(it.data)
                }
            }
        }
    }
}