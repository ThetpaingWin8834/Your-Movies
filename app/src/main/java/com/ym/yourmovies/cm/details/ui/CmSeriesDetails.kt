package com.ym.yourmovies.cm.details.ui

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ym.yourmovies.R
import com.ym.yourmovies.cm.details.data.CmSeriesDetailViewmodel
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.download.ui.CMdownDia
import com.ym.yourmovies.download.ui.CmClickEvent
import com.ym.yourmovies.ui.theme.ShimmerColor
import com.ym.yourmovies.ui.watch.data.WatchServer
import com.ym.yourmovies.utils.components.*
import com.ym.yourmovies.utils.models.DownloadItem
import com.ym.yourmovies.utils.models.NameAndUrlModel
import com.ym.yourmovies.utils.others.*


@Composable
fun CmSeriesDetails(
    modifier: Modifier = Modifier,
    movie: CmMovie,
    context: Activity,
    viewModel: CmSeriesDetailViewmodel =viewModel(),
    onDownloadOrWatch: (event: CmClickEvent, url: String, watchServer: WatchServer) -> Unit,
    onDownloadByDm: (String) -> Unit
    ) {
    LoadingAndError(
        isLoading = viewModel.isLoading,
        error = viewModel.error,
        onErrorClick = {
            viewModel.getData(movie)
        })
    val data = viewModel.data.value
    var downloadItem by remember {
        mutableStateOf<DownloadItem?>(null)
    }
    val onDownload = remember<(NameAndUrlModel)->Unit> {
        {
            downloadItem = DownloadItem(
                title = movie.title,
                server = it.text,
                thumb = movie.thumb,
                serverIcon = "",
                more = emptyList(),
                url = it.url
            )
        }
    }
    if (data!=null){
        Column(modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            CmSeriesHeader(bigThumb = data.bigThumb, thumb = movie.thumb, description = data.description)
            CmSeriesDetailsBody(
                htmlText = data.htmlText,
                onDownload =onDownload
            )
            DescriptionTitle(title = stringResource(id = R.string.related))
           MyRelated(list = data.relatedList, onItemClick = {Goto.cmDetails(context,it.toBundle())})
        }
    }


    if (downloadItem != null) {
        CMdownDia(title = movie.title, downloadItem = downloadItem!!, onDismiss = {
            downloadItem = null
        },
            onDownloadByDm = onDownloadByDm,
        onDownloadOrWatch = onDownloadOrWatch)

    }
    LaunchedEffect(key1 = true){
        viewModel.getData(movie)
    }
}

@Composable
private fun CmSeriesHeader(
    bigThumb: String,
    thumb: String,
    description: String
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = bigThumb,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
                .clip(RoundedCornerShape(7.dp)),
            placeholder = ColorPainter(ShimmerColor),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            AsyncImage(
                model = thumb,
                contentDescription = "thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .aspectRatio(0.65f)
                    .offset(y = (-25).dp, x = 15.dp)
                    .clip(RoundedCornerShape(7.dp))

            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = description, modifier = Modifier
                    .offset(x = 15.dp)
            )
        }
    }

}

@Composable
private fun CmSeriesDetailsBody(
    htmlText: String,
    onDownload:(NameAndUrlModel)->Unit
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
                    a{
            display: inline-block; 
            width: 100px;
           height: 40px; 
            text-align: center;
         font-size: medium;
           text-overflow: ellipsis;
           max-lines: 1;
            line-height: 40px;
            text-decoration: none;
            background-color:  cornflowerblue;
            margin-bottom: 5px;
            color: white;
            border-radius: 5px;
        }
        a:hover{
            background-color: orchid;
      border-radius: 5px;
        }
    img{display:inline;height:auto;width:100%;}
               </style>
           </head>
           <body>
               
              $htmlText 
           </body>
           </html>
       """
    }

    Spacer(modifier = Modifier.height(10.dp))

    MyWebView(html = html,isAddJavaScript = true){ link,server->
         onDownload(NameAndUrlModel(text = server, url = link))
    }
}