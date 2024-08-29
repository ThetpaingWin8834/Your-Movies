package com.ym.yourmovies.ui.watch.model

import com.ym.yourmovies.utils.models.NameAndUrlModel

data class WatchableItem(
    val server:Server,
    val listOfQualityAndUrls :List<NameAndUrlModel>
)