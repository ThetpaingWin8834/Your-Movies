package com.ym.yourmovies.cm.seeall.adapters

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
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.toBundle

class CmSeeAllRecyAdapter : PagingDataAdapter<CmMovie, CmSeeAllRecyAdapter.CmSeeAllRecyVH>(
    object  : DiffUtil.ItemCallback<CmMovie>(){
        override fun areItemsTheSame(oldItem: CmMovie, newItem: CmMovie): Boolean =oldItem.url==newItem.url

        override fun areContentsTheSame(oldItem: CmMovie, newItem: CmMovie): Boolean =oldItem.url==newItem.url

    }
) {

   inner class CmSeeAllRecyVH ( mView:View) : RecyclerView.ViewHolder(mView){
         val thumb : ImageView = mView.findViewById(R.id.cmIvThumb)
        val title : TextView = mView.findViewById(R.id.cmTvTitle)
        val rating : TextView = mView.findViewById(R.id.cmTvRate)
        init {
            itemView.setOnClickListener {
                val item = getItem(absoluteAdapterPosition)
                if (item!=null) {
                    Goto.cmDetails(itemView.context,item.toBundle())
                }

            }
        }
    }

    override fun onBindViewHolder(holder: CmSeeAllRecyVH, position: Int) {
        val movie = getItem(position)
        if (movie!=null){
            holder.thumb.load(data = movie.thumb){
                placeholder(R.color.shimmer_color)
                    crossfade(true)
            }
            holder.title.text=movie.title
            holder.rating.text=movie.rating
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CmSeeAllRecyVH {
        return CmSeeAllRecyVH(
            LayoutInflater.from(parent.context).inflate(R.layout.recy_cm_item,parent,false)
        )
    }
}