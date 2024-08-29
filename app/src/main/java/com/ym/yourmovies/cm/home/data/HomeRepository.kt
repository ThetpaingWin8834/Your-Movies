package com.ym.yourmovies.cm.home.data

import com.ym.yourmovies.cm.home.models.CmHomeData
import com.ym.yourmovies.utils.models.Response
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
      suspend fun getData() :Flow<Response<CmHomeData>>
}