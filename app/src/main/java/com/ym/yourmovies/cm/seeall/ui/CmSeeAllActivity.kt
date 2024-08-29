package com.ym.yourmovies.cm.seeall.ui

import androidx.activity.viewModels
import androidx.paging.PagingDataAdapter
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.cm.seeall.data.CmSeeAllViewModel
import com.ym.yourmovies.cm.seeall.adapters.CmSeeAllLinearRecyAdapter
import com.ym.yourmovies.cm.seeall.adapters.CmSeeAllRecyAdapter
import com.ym.yourmovies.utils.abstracts.AbstractSeeallActivity

class CmSeeAllActivity : AbstractSeeallActivity<CmMovie>(){
    override val viewModel:CmSeeAllViewModel
        get() = viewModels<CmSeeAllViewModel>().value

    override fun getPagingAdapter(): PagingDataAdapter<CmMovie, *> {
        return lazy {
            if (isLinear){
                CmSeeAllLinearRecyAdapter()
            } else{
                CmSeeAllRecyAdapter()
            }

        }.value
    }
}