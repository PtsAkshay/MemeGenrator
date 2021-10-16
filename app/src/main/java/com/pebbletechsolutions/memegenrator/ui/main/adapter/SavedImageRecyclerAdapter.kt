package com.pebbletechsolutions.memegenrator.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.FdbMemeModel
import com.pebbletechsolutions.memegenrator.data.model.SavedImageModel
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner
import kotlinx.android.synthetic.main.saved_img_item_lyt.view.*

class SavedImageRecyclerAdapter(val favImg: ArrayList<FdbMemeModel>, val OnFavListClick: OnRecyclerItemClickListner):
    RecyclerView.Adapter<SavedImageRecyclerAdapter.SavedImgViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImgViewHolder {
        return SavedImgViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.saved_img_item_lyt, parent, false), OnFavListClick
        )
    }

    override fun onBindViewHolder(holder: SavedImgViewHolder, position: Int) {
        val SImg = favImg[position]
        Glide.with(holder.itemView.context).load(SImg.image).into(holder.itemView.savedImgView)
    }

    override fun getItemCount(): Int {
        return favImg.size
    }

    class SavedImgViewHolder(itemView: View, onFIClick: OnRecyclerItemClickListner): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onFIClick.onItemClick(absoluteAdapterPosition)
            }
        }
    }
}