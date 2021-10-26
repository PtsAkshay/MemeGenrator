package com.pebbletechsolutions.memegenrator.ui.main.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.SavedModel
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner

class SavedImageRecyclerAdapter(val onItemClick: OnRecyclerItemClickListner):
    RecyclerView.Adapter<SavedImageRecyclerAdapter.SavedImageViewHolder>(){

    val allSavedImgs = ArrayList<SavedModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedImageRecyclerAdapter.SavedImageViewHolder {
        return SavedImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.saved_img_item_lyt, parent, false), onItemClick
        )
    }

    override fun onBindViewHolder(
        holder: SavedImageRecyclerAdapter.SavedImageViewHolder,
        position: Int
    ) {
        val currentImgPath = allSavedImgs[position]
//        holder.SavImg.setImageURI(currentImgPath.SavImgUri.toUri())
        Glide.with(holder.itemView).load(currentImgPath.SavImgUri.toUri()).into(holder.SavImg)
    }

    override fun getItemCount(): Int {
        return allSavedImgs.size
    }

    fun updateSavedImgList(newList: List<SavedModel>){
        allSavedImgs.clear()
        allSavedImgs.addAll(newList)

        notifyDataSetChanged()
    }

    class SavedImageViewHolder(itemView: View, onSavItemClick: OnRecyclerItemClickListner): RecyclerView.ViewHolder(itemView) {
        val SavImg: RoundedImageView = itemView.findViewById(R.id.savedImgView)

        init {
            itemView.setOnClickListener {
                onSavItemClick.onItemClick(absoluteAdapterPosition)
            }
        }
    }
}


