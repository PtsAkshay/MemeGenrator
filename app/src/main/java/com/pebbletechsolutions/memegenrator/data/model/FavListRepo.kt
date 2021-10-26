package com.pebbletechsolutions.memegenrator.data.model

import androidx.lifecycle.LiveData

class FavListRepo(private val favDo: FavAndSavedDao) {

    val allUrls: LiveData<List<FavModel>> = favDo.getAllFavImg()

    val allSavedUris: LiveData<List<SavedModel>> = favDo.getAllSavedImg()

    suspend fun insertFavouriteImg(favImg: FavModel){
        favDo.insertFavImg(favImg)
    }

    suspend fun insertSavedImg(SavImg: SavedModel){
        favDo.insertSavedImg(SavImg)
    }

    suspend fun deleteFavouriteImg(favImg: FavModel){
        favDo.deleteFavImg(favImg)
    }


}