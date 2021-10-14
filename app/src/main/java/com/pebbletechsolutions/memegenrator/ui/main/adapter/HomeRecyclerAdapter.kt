package com.pebbletechsolutions.memegenrator.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.MemeModel
import com.pebbletechsolutions.memegenrator.utils.HoemDialogClickLisnter
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner
import kotlinx.android.synthetic.main.home_recycler_item.view.*

class HomeRecyclerAdapter(val urls: ArrayList<MemeModel>, private val onItemClick: OnRecyclerItemClickListner):
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeRVViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRVViewHolder {
        return HomeRVViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_item, parent, false), onItemClick
        )
    }

    override fun onBindViewHolder(holder: HomeRVViewHolder, position: Int) {
        val url = urls[position]
        holder.itemView.memeImg.setImageResource(url.memeImg)
    }

    override fun getItemCount(): Int {
        return urls.size
    }

    class HomeRVViewHolder(ItemView: View, onItemClick: OnRecyclerItemClickListner): RecyclerView.ViewHolder(ItemView){
        init {
            ItemView.setOnClickListener {
                onItemClick.onItemClick(absoluteAdapterPosition)
            }
        }
    }
}