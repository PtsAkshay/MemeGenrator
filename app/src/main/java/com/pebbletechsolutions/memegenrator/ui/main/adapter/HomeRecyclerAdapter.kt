package com.pebbletechsolutions.memegenrator.ui.main.adapter

import android.content.ClipData
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.FdbMemeModel
import com.pebbletechsolutions.memegenrator.data.model.MemeModel
import com.pebbletechsolutions.memegenrator.utils.HoemDialogClickLisnter
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner
import kotlinx.android.synthetic.main.home_recycler_item.view.*

class HomeRecyclerAdapter(val urls: ArrayList<FdbMemeModel>, val OnItemClick: OnRecyclerItemClickListner):
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeRVViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRVViewHolder {
        return HomeRVViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_item, parent, false), OnItemClick
        )
    }

    override fun onBindViewHolder(holder: HomeRVViewHolder, position: Int) {
        val url = urls[position]
//        holder.MemeImg.setImageResource(url.memeImg)
        Glide.with(holder.itemView).load(url.image).into(holder.MemeImg)

    }

    override fun getItemCount(): Int {
        return urls.size
    }

    class HomeRVViewHolder(ItemView: View, onItemClick: OnRecyclerItemClickListner): RecyclerView.ViewHolder(ItemView){

        val MemeImg: RoundedImageView = ItemView.findViewById(R.id.memeImg)

        init {
            ItemView.setOnClickListener {
                onItemClick.onItemClick(absoluteAdapterPosition)
            }
        }

    }
}