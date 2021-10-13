package com.pebbletechsolutions.memegenrator.ui.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pebbletechsolutions.memegenrator.data.model.SavedImageModel

class SavedImageRecyclerAdapter(val Img: ArrayList<SavedImageModel>):
    RecyclerView.Adapter<SavedImageRecyclerAdapter.SavedImgViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImgViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: SavedImgViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return Img.size
    }

    class SavedImgViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}