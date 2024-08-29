package com.ym.yourmovies.ui.search.random

import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.utils.models.Channel

data class RandomRecommandData(
    val categoryList: List<CategoryItem> = emptyList(),
    val randomList : List<Any> = emptyList(),
    val isLoading:Boolean = false,
    val error:Exception?=null,
    val channel: Channel = Channel.Unknown
)