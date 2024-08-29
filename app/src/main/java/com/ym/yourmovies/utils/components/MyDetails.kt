package com.ym.yourmovies.utils.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.ym.yourmovies.R
import com.ym.yourmovies.YmApp
import com.ym.yourmovies.utils.components.ads.ApplovinBanner
import com.ym.yourmovies.utils.components.ads.NativeAdView
import com.ym.yourmovies.utils.components.ads.rememberNativeAdsLoader
import com.ym.yourmovies.utils.models.CastMetaData
import com.ym.yourmovies.utils.models.DetailsBodyData
import com.ym.yourmovies.utils.models.NameAndUrlModel

@Composable
inline fun < reified Movie : Any>MyDetails(
    modifier : Modifier = Modifier,
    headerData: MyDetailsHeaderData,
    detailsBodyDataList: List<DetailsBodyData>,
    relatedList: List<Movie>,
    iscm:Boolean,
    moreMeta: @Composable () -> Unit,
    backdrops: @Composable () -> Unit,
    download: @Composable () -> Unit,
    noinline onRelatedClick: (Movie) -> Unit,
    noinline onCastClick : (CastMetaData)->Unit,
    crossinline onGenreClick: (NameAndUrlModel) -> Unit
) {
    val nativeAdsMedium by rememberNativeAdsLoader(nativeId = YmApp.nativeId)
    val nativeAdsSmall by rememberNativeAdsLoader(nativeId = YmApp.nativeSmallId)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp)
            .verticalScroll(state = rememberScrollState())
    ) {

        if (detailsBodyDataList.isNotEmpty()){
            MyDetailsHeader(data = headerData, onGenreClick = onGenreClick, moreMeta = moreMeta)
            Spacer(modifier = Modifier.height(5.dp))
            MyDivider()
            DetailsBody(detailsBody = detailsBodyDataList, isCm = iscm, onCastClick = onCastClick)
            backdrops()
            if (nativeAdsSmall!=null){
                NativeAdView(maxAd = nativeAdsSmall!!)
            }
            download()
            MyRelated(list = relatedList, onItemClick = onRelatedClick)
            if (nativeAdsMedium!=null){
                NativeAdView(maxAd = nativeAdsMedium!!)
            }
           EndDivider()
        }
    }
}

@Composable
inline fun MyDetailsHeader(
    data: MyDetailsHeaderData,
    crossinline onGenreClick: (NameAndUrlModel) -> Unit,
    moreMeta: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth() ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyAsyncImage(
            thumbUrl =data.thumb,
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .aspectRatio(0.7f)
            )
        Spacer(modifier = Modifier.width(7.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.titleLarge ,
            )
            Spacer(modifier = Modifier.height(5.dp))
            if (data.fullTitle.isNotEmpty() || data.date.isNotEmpty() || data.duration.isNotEmpty()) {
                FlowRow(
                   // mainAxisSpacing = 5.dp,
                   crossAxisSpacing =5.dp,
                    crossAxisAlignment = FlowCrossAxisAlignment.Center
                ) {

                    Text(
                        text = data.fullTitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    if (data.date.isNotBlank()){
                        IconAndText(id = R.drawable.ic_date, text = data.date,fontWeight = FontWeight.Light)
                    }

                    if (data.duration.isNotBlank()) {
                        IconAndText(id = R.drawable.ic_time, text = data.duration)
                    }
                    if (data.country.isNotBlank()){
                      IconAndText(id = R.drawable.ic_network, text = data.country)
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
            }
            ////


            if (data.rating.isNotBlank() && data.rating!="0") {
                Spacer(modifier = Modifier.height(7.dp))
                RatingWithVotes(
                    ratingCount = data.rating,
                    ratingWithVotes = data.ratingWithTotal
                )
                Spacer(modifier = Modifier.height(7.dp))

            }
            FlowRow(
                mainAxisSpacing = 3.dp,
                crossAxisSpacing = 3.dp,
            ) {
                for (genre in data.genresList) {
                    Text(text = HtmlCompat.fromHtml(genre.text,HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .border(
                                0.5.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                            .clickable {
                                if (genre.url.isNotBlank()) {
                                    onGenreClick(genre)
                                }

                            })
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
           moreMeta()
        }


    }

}
data class MyDetailsHeaderData(
   val title:String,
   val thumb:String,
   val rating:String,
   val ratingWithTotal:String,
   val fullTitle:String,
   val date:String,
   val duration:String,
   val country:String,
   val genresList:List<NameAndUrlModel>
){
    companion object {
        fun emptyData()=MyDetailsHeaderData(
            title = "",
            thumb = "",
            rating = "",
            ratingWithTotal = "",
            fullTitle = "",
            date = "",
            duration = "",
            country = "",
            genresList = emptyList()
        )
    }
}