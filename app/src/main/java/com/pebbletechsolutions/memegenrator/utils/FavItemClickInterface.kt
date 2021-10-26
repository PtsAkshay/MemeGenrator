package com.pebbletechsolutions.memegenrator.utils

import com.pebbletechsolutions.memegenrator.data.model.FavModel

interface FavItemClickInterface {
    fun onFavItemClick(FavImg: FavModel)
}