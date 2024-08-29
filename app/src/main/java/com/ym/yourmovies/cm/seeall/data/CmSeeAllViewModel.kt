package com.ym.yourmovies.cm.seeall.data

import androidx.paging.PagingSource
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.abstracts.AbstractSeeAllViewModel

class CmSeeAllViewModel : AbstractSeeAllViewModel<CmMovie>() {
   
    override val pagingSourceFactory: () -> PagingSource<Int, CmMovie>
        get() = {
            CmSeeAllPagingSource(data = pageAndQuery.value!!, currPage = currPage)

        }
}