package com.ym.yourmovies.msub.seeall.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ym.yourmovies.R
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.toBundle

class MsubSeeAllLinearRecyAdapter : PagingDataAdapter<MSubMovie, MsubSeeAllLinearRecyAdapter.MsubSeeAllLinearRecyVH>(
    object  : DiffUtil.ItemCallback<MSubMovie>(){
        override fun areItemsTheSame(oldItem: MSubMovie, newItem: MSubMovie): Boolean =oldItem.url==newItem.url

        override fun areContentsTheSame(oldItem: MSubMovie, newItem: MSubMovie): Boolean =oldItem.url==newItem.url

    }
) {

  inner  class MsubSeeAllLinearRecyVH (view:View) : RecyclerView.ViewHolder(view){
         val thumb : ImageView = view.findViewById(R.id.linearRecyMsubThumb)
        val title : TextView = view.findViewById(R.id.linearRecyMsubTvTitle)
        val rating : TextView = view.findViewById(R.id.linearRecyMsubTvTitle)
        val date : TextView = view.findViewById(R.id.linearRecyMsubTvDate)
        init {
            itemView.setOnClickListener {
                val item = getItem(absoluteAdapterPosition)
                if (item!=null) {
                    Goto.msubDetails(itemView.context,item.toBundle())
                }

            }
        }
    }

    override fun onBindViewHolder(holder: MsubSeeAllLinearRecyVH, position: Int) {
           val movie = getItem(position)
        if (movie!=null){
            holder.thumb.load(data = movie.thumb){
                placeholder(R.color.shimmer_color)
                    crossfade(true)
            }
            holder.title.text=movie.title
            holder.rating.text=movie.rating
            holder.date.text=movie.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsubSeeAllLinearRecyVH {
        return MsubSeeAllLinearRecyVH(
            LayoutInflater.from(parent.context).inflate(R.layout.recy_msub_item_linear,parent,false)
        )
    }
}