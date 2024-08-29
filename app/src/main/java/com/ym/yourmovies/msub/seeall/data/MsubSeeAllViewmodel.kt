package com.ym.yourmovies.msub.seeall.data

import androidx.paging.PagingSource
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.utils.abstracts.AbstractSeeAllViewModel

class MsubSeeAllViewmodel : AbstractSeeAllViewModel<MSubMovie>() {
    override val pagingSourceFactory: () -> PagingSource<Int, MSubMovie>
        get() = {
            MsubSeeAllPagingSource(data = pageAndQuery.value!!, currPage = currPage)
        }
}