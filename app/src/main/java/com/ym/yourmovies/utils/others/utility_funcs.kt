package com.ym.yourmovies.utils.others

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.ym.yourmovies.gc.details.models.GcMovieDownloadData
import com.ym.yourmovies.ui.watch.model.Server
import com.ym.yourmovies.ui.watch.model.WatchModel
import com.ym.yourmovies.ui.watch.model.WatchableItem
import com.ym.yourmovies.utils.models.NameAndUrlModel


fun checkCanWatch(
    list: List<WatchModel>
): List<WatchableItem> {
    val temp = list.filter {
        it.server.serverName.isContainTexts(
            listOf(
                "g drive",
                "gdrive",
                "googledrive",
                "google drive",
                "drive.google",
                "megaup",
                "mega up",
                "yoteshin",
                "mediafire"
            )
        )
    }
    return convertToWatchableList(temp)
}

private fun convertToWatchableList(list: List<WatchModel>): List<WatchableItem> {
    val megaList = mutableListOf<WatchModel>()
    val yoteShinList = mutableListOf<WatchModel>()
    val gdriveList = mutableListOf<WatchModel>()
    val mediaFireList = mutableListOf<WatchModel>()
    for (item in list) {
        if (item.server.serverName.isContainMegaUp()) {
            megaList.add(item.copy(server = item.server.copy(serverName = "MegaUp")))
        } else if (item.server.serverName.isContainYoteShin()) {
            yoteShinList.add(item.copy(server = item.server.copy(serverName = "YoteShinDrive")))
        } else if (item.server.serverName.isContainGdrive()) {
            gdriveList.add(item.copy(server = item.server.copy(serverName = "Gdrive")))
        }
        else if (item.server.serverName.isContainMediaFire()) {
            mediaFireList.add(item.copy(server = item.server.copy(serverName = "Mediafire")))
        }
        else {
            continue
        }
    }
    val map = (megaList+mediaFireList + yoteShinList + gdriveList).groupBy { it.server }
    val watchableLIst = mutableListOf<WatchableItem>()
    for (key in map.keys) {
        val temp = map[key]
        if (temp != null) {
            watchableLIst.add(
                WatchableItem(
                    server = key,
                    listOfQualityAndUrls = temp.map { NameAndUrlModel(text = it.quality, it.url) }
                )
            )
        }
    }


    return watchableLIst
}

//fun String.getCommonWordFrom(nextWord: String): String {
//    val word1 = this.split(" ")
//    val word2 = nextWord.split(" ")
//    for (word in word1) {
//        if (word2.contains(word)) {
//            return word
//        }
//    }
//    return this
//}

fun String.isContainMegaUp(): Boolean {
    val pattern = listOf("megaup", "mega up").joinToString("|").toRegex(RegexOption.IGNORE_CASE)
    return pattern.containsMatchIn(this)
}
fun String.isContainMediaFire(): Boolean {
    val pattern = "mediafire".toRegex(RegexOption.IGNORE_CASE)
    return pattern.containsMatchIn(this)
}

fun String.isContainYoteShin(): Boolean {
    val pattern = "yoteshin".toRegex(RegexOption.IGNORE_CASE)
    return pattern.containsMatchIn(this)
}
fun colorToRgbText(color:Color):String = with(color){
    "rgb(${red * 255},${green * 255},${blue * 255})"
}
fun GcMovieDownloadData.toWatchModel(): WatchModel {
    val qualityPattern = "(\\d+p)|(\\d+)(\\s)p".toRegex(RegexOption.IGNORE_CASE)
    val mQuality = qualityPattern.find(this.quality)?.value
    if (mQuality.isNullOrEmpty()) {
        return WatchModel(
            server = Server(
                serverIcon = this.serverIcon,
                serverName = this.quality
            ),
            quality = this.quality,
            url = this.url
        )
    } else {
        val mServerName = this.quality.replace(mQuality, "")
        return WatchModel(
            server = Server(
                serverName = mServerName,
                serverIcon = this.serverIcon
            ),
            quality = mQuality,
            url = this.url
        )
    }

}

fun String.isContainGdrive(): Boolean {
    val pattern = listOf(
        "g drive",
        "gdrive",
        "googledrive",
        "google drive",
        "drive.google"
    ).joinToString("|").toRegex(RegexOption.IGNORE_CASE)

    return pattern.containsMatchIn(this)
}

fun String.isContainTexts(list: List<String>): Boolean {
    val pattern = list.joinToString("|").toRegex(RegexOption.IGNORE_CASE)

    return pattern.containsMatchIn(this)
}