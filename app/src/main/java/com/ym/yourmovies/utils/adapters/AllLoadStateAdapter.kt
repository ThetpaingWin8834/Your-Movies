package com.ym.yourmovies.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.others.NotFoundException
import org.jsoup.HttpStatusException

class AllLoadStateAdapter(
    val retry: () -> Unit) :
    LoadStateAdapter<AllLoadStateAdapter.LoadStateVh>() {
    inner class LoadStateVh(view:View) :
        RecyclerView.ViewHolder(view) {
        private val footerPb :ProgressBar = view.findViewById(R.id.footerPb)
        private val footerBt :Button = view.findViewById(R.id.footerBt)
        private val footerTv :TextView = view.findViewById(R.id.footerTv)
        init {
            footerBt.setOnClickListener {

                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading){
                footerPb.isVisible=true
                footerBt.isVisible=false
                footerTv.isVisible=false
            }
            if (loadState is LoadState.NotLoading){
                footerPb.isVisible=false
            }
            if (loadState is LoadState.Error){
                footerTv.isVisible=true
                footerPb.isVisible=false
                if (loadState.error is HttpStatusException || loadState.error is NotFoundException){
                     footerTv.setText(R.string.not_found_anymore)
                }else{
                    footerTv.setText( R.string.try_vpn)
                    footerBt.isVisible=true
                }
            }


        }
    }

    override fun onBindViewHolder(holder: LoadStateVh, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateVh {
        return LoadStateVh(
                LayoutInflater.from(parent.context).inflate(R.layout.load_state_footer,parent,
                    false)


        )
    }

}