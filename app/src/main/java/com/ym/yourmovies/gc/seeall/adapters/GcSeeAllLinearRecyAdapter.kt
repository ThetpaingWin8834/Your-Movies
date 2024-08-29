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
import com.ym.yourmovies.cm.seeall.model.GcMovieMeta
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.toBundle

class GcSeeAllLinearRecyAdapter : PagingDataAdapter<GcMovieMeta, GcSeeAllLinearRecyAdapter.GcSeeAllRecyVHLinear>(
    object  : DiffUtil.ItemCallback<GcMovieMeta>(){
        override fun areItemsTheSame(oldItem: GcMovieMeta, newItem: GcMovieMeta): Boolean =oldItem.url==newItem.url

        override fun areContentsTheSame(oldItem: GcMovieMeta, newItem: GcMovieMeta): Boolean =oldItem.url==newItem.url

    }
) {

   inner class GcSeeAllRecyVHLinear (view:View) : RecyclerView.ViewHolder(view){
         val thumb : ImageView = view.findViewById(R.id.linearRecyGcThumb)
        val title : TextView = view.findViewById(R.id.linearRecyGcTvTitle)
        val rating : TextView = view.findViewById(R.id.linearRecyGcTvRating)
        val duration : TextView = view.findViewById(R.id.linearRecyGcTvDuration)
        val views : TextView = view.findViewById(R.id.linearRecyGcTvViews)
        val desc : TextView = view.findViewById(R.id.linearRecyGcTvDesc)
        val genres : TextView = view.findViewById(R.id.linearRecyGcTvGenres)
        val date : TextView = view.findViewById(R.id.linearRecyGcTvDate)
        init {
            itemView.setOnClickListener {
                val item = getItem(absoluteAdapterPosition)
                if (item!=null) {
                    Goto.gcDetails(itemView.context,item.toBundle())
                }

            }
        }
    }

    override fun onBindViewHolder(holder: GcSeeAllRecyVHLinear, position: Int) {
           val movie = getItem(position)
        if (movie!=null){
            holder.thumb.load(data = movie.thumb){
                placeholder(R.color.shimmer_color)
                    crossfade(true)
            }
            holder.title.text=movie.title
            holder.rating.text=movie.rating
            holder.date.text=movie.date
            holder.duration.text = movie.duration
            holder.views.text = movie.views
            holder.desc.text = movie.desc
            holder.genres.text = movie.genres
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GcSeeAllRecyVHLinear {
        return GcSeeAllRecyVHLinear(
            LayoutInflater.from(parent.context).inflate(R.layout.recy_gc_item_linear,parent,false)
        )
    }
}