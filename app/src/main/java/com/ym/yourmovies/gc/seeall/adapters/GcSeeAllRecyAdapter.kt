package com.ym.yourmovies.gc.seeall.adapters

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
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.toBundle

class GcSeeAllRecyAdapter : PagingDataAdapter<GcMovie, GcSeeAllRecyAdapter.GcSeeAllRecyVH>(
    object  : DiffUtil.ItemCallback<GcMovie>(){
        override fun areItemsTheSame(oldItem: GcMovie, newItem: GcMovie): Boolean =oldItem.url==newItem.url

        override fun areContentsTheSame(oldItem: GcMovie, newItem: GcMovie): Boolean =oldItem.url==newItem.url

    }
) {

   inner class GcSeeAllRecyVH (view:View) : RecyclerView.ViewHolder(view){
         val thumb : ImageView = view.findViewById(R.id.otherIvThumb)
        val title : TextView = view.findViewById(R.id.othervTitle)
        val rating : TextView = view.findViewById(R.id.otherTvRate)
        val date : TextView = view.findViewById(R.id.otherTvDate)
        init {
            itemView.setOnClickListener {
                val item = getItem(absoluteAdapterPosition)
                if (item!=null) {
                    Goto.gcDetails(itemView.context,item.toBundle())
                }

            }
        }
    }

    override fun onBindViewHolder(holder: GcSeeAllRecyVH, position: Int) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GcSeeAllRecyVH {
        return GcSeeAllRecyVH(
            LayoutInflater.from(parent.context).inflate(R.layout.recy_gc_msub_item,parent,false)
        )
    }
}