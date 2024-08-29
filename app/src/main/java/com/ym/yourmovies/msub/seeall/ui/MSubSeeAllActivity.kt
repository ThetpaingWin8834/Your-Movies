package com.ym.yourmovies.msub.seeall.ui

import androidx.activity.viewModels
import androidx.paging.PagingDataAdapter
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.msub.seeall.adapters.MsubSeeAllLinearRecyAdapter
import com.ym.yourmovies.msub.seeall.adapters.MsubSeeAllRecyAdapter
import com.ym.yourmovies.msub.seeall.data.MsubSeeAllViewmodel
import com.ym.yourmovies.utils.abstracts.AbstractSeeAllViewModel
import com.ym.yourmovies.utils.abstracts.AbstractSeeallActivity

class MSubSeeAllActivity : AbstractSeeallActivity<MSubMovie>() {
    override val viewModel: AbstractSeeAllViewModel<MSubMovie>
        get() = viewModels<MsubSeeAllViewmodel> () .value
    override fun getPagingAdapter(): PagingDataAdapter<MSubMovie, *> {
       return lazy {
           if (isLinear){
              MsubSeeAllLinearRecyAdapter()
           } else{
               MsubSeeAllRecyAdapter()
           }

       }.value
    }
}