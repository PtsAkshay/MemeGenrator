package com.pebbletechsolutions.memegenrator.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.FavModel
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner

class FavouriteRecyclerAdapter(val onFavItemClick: OnRecyclerItemClickListner):
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>()
{

    val allImgUrls = ArrayList<FavModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.favourite_item_lyt, parent, false), onFavItemClick
        )
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val curImg = allImgUrls[position]
        Glide.with(holder.itemView).load(curImg.FavImgUri).into(holder.FavImg)
    }

    override fun getItemCount(): Int {
       return allImgUrls.size
    }

    fun updateList(newList: List<FavModel>){
        allImgUrls.clear()
        allImgUrls.addAll(newList)

        notifyDataSetChanged()
    }

    class FavouriteViewHolder(itemView: View, onFaVItemClick: OnRecyclerItemClickListner): RecyclerView.ViewHolder(itemView) {
        val FavImg: RoundedImageView = itemView.findViewById(R.id.favImgView)
        init {
            itemView.setOnClickListener {
                onFaVItemClick.onItemClick(absoluteAdapterPosition)
            }
        }
    }



}