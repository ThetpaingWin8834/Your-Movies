package com.ym.yourmovies.utils.components

import android.content.Context
import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.util.LinkifyCompat
import com.google.accompanist.flowlayout.FlowRow
import com.ym.yourmovies.ui.settings.MySettingsManager
import com.ym.yourmovies.ui.settings.autoMode
import com.ym.yourmovies.ui.settings.lightMode
import com.ym.yourmovies.utils.models.CastMetaData
import com.ym.yourmovies.utils.models.Casts
import com.ym.yourmovies.utils.models.DetailsBodyData
import com.ym.yourmovies.utils.models.DetailsBodyModel
import com.ym.yourmovies.utils.others.Ym
import com.ym.yourmovies.utils.others.colorToRgbText

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsBody(
    detailsBody: List<DetailsBodyData>,
    isCm: Boolean,
    onCastClick: (CastMetaData) -> Unit
) {

    var selectedIndex by remember {
        mutableStateOf(0)
    }
    TabRow(selectedTabIndex = selectedIndex) {
        detailsBody.forEachIndexed { index, _ ->
            Tab(selected = selectedIndex == index, onClick = {
                selectedIndex = index
            }, text = {
                Text(
                    text = detailsBody[index].title,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            })
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    AnimatedContent(targetState = selectedIndex) { index ->

        when (val bodyData = detailsBody[index].detailsBodyModel) {
            is DetailsBodyModel.Synopsis -> {
                Synopsis(
                    htmlString = bodyData.htmlString,
                    //backDropList = backDropList,
                    isCm = isCm
                )
            }
            is DetailsBodyModel.AllCasts -> {
                Column {
                    if (isCm) {
                        bodyData.allCastsList.forEach { casts ->
                            MovieDetailCastCm(casts = casts, onCastClick = onCastClick)
                        }
                    } else {
                        bodyData.allCastsList.forEach { casts ->
                            MovieDetailCast(casts = casts, onCastClick = onCastClick)
                        }
                    }

                }

            }

            is DetailsBodyModel.Trailers -> {
                TrailerWebView(trailers = bodyData.trailersList)
            }
            is DetailsBodyModel.Nothing -> {}
        }

    }


}


@Composable
private fun TrailerWebView(trailers: List<String>,context: Context = LocalContext.current) {

//    var backgroundColor = "rgb(31, 31, 31)"
//    var textColor = "whitesmoke"
//    when(MySettingsManager.getTheme(context)){
//        lightMode ->{
//            backgroundColor = "white"
//            textColor = "black"
//        }
//        autoMode ->{
//            if (!isSystemInDarkTheme()) {
//                backgroundColor = "white"
//                textColor = "black"
//            }
//        }
//    }
    val backgroundColor = colorToRgbText(MaterialTheme.colorScheme.background)
    val textColor = colorToRgbText(LocalContentColor.current)
    BoxWithConstraints {
        val htmlText = remember {
            var string = ""
            trailers.forEach {
                string =
                    "$string\n <iframe src=$it frameborder=\"0\" width=\"${maxWidth}\" height=\"${maxWidth / 2f}\"></iframe> " +
                            " <div style=\"background-color: grey; width: 100%; height: 1px;\"></div>"
            }

            """
        <!DOCTYPE html>
        <html>
        <head>
           <meta charset='utf-8'>
            <meta name='viewport' content='width=device-width, initial-scale=1'>
            <style>
               body{
               width: 100%;
             background-color: $backgroundColor;
                       color: $textColor;
        }
           iframe{
           margin: 18px;
        }
        </style>
        </head>
        <body">
           $string
        </body>
        </html>
    """
        }
        MyWebView(html = htmlText)
    }


}

@Composable
private fun Synopsis(
    htmlString: String,
    isCm: Boolean,
    context: Context = LocalContext.current
) {

    val backgroundColor = colorToRgbText(MaterialTheme.colorScheme.background)
    val textColor = colorToRgbText(LocalContentColor.current)
    val html = remember {
        """
           <!DOCTYPE html>
           <html>
           <head>
               <meta charset='utf-8'>
               <meta name='viewport' content='width=device-width, initial-scale=1'>
               <style>
                   body{
                       background-color: $backgroundColor;
                       color: $textColor;
                   }
                   
    img{display:inline;height:auto;width:100%;}
               </style>
           </head>
           <body>
               
              $htmlString
           </body>
           </html>
       """
    }
    if (isCm) {
        MyWebView(html = html, isAddJavaScript = true, onGenerate = { link, title ->
            Ym.download(context = context, link = link)
        })
    } else {
        val textView = remember {
            TextView(context)
        }
        AndroidView(factory = { textView }, update = { tv ->
            tv.text = htmlString
            LinkifyCompat.addLinks(tv, Linkify.WEB_URLS)
        })
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailCastCm(
    casts: Casts,
    onCastClick: (CastMetaData) -> Unit

) {
    FlowRow {
        casts.castList.forEach { metaData ->
            CastMeta(metaData = metaData, onCastClick = onCastClick)
        }

    }

}

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
private fun MovieDetailCast(
    casts: Casts,
    onCastClick: (CastMetaData) -> Unit

) {
    Column(
        modifier = Modifier.padding(7.dp)
    ) {
        Text(text = casts.title, style = MaterialTheme.typography.titleLarge)
        MyDivider()

        casts.castList.forEach { castMetaData ->
            CastMetaWithIcon(metaData = castMetaData, onCastClick = onCastClick)
        }


        //  Divider()
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
private fun CastMeta(
    metaData: CastMetaData,
    onCastClick: (CastMetaData) -> Unit
) {
    Text(
        text = metaData.realname,
        modifier = Modifier
            .padding(5.dp)
            .clickable {
                onCastClick(metaData)
            }
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(5.dp)

    )
}

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
private fun CastMetaWithIcon(
    metaData: CastMetaData,
    onCastClick: (CastMetaData) -> Unit


) {
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCastClick(metaData) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyAsyncImage(thumbUrl = metaData.thumb ?: "", modifier = Modifier.size(70.dp))
        Spacer(modifier = Modifier.width(7.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = metaData.realname, fontWeight = FontWeight.Bold)
            if (metaData.movieName != null) {
                Text(
                    text = metaData.movieName,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}