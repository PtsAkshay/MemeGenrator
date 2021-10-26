package com.pebbletechsolutions.memegenrator.data.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavListViewModel(application: Application): AndroidViewModel(application) {
    val repository: FavListRepo
    val allImgs: LiveData<List<FavModel>>
    val allSavedImgs: LiveData<List<SavedModel>>

    init {
        val dao = MemeFavAndSavedListsDatabase.getMemeDatabase(application).getFavAndSavedDao()
        repository = FavListRepo(dao)
        allImgs = repository.allUrls
        allSavedImgs = repository.allSavedUris
    }

    fun deleteFavrtImg(favImg: FavModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFavouriteImg(favImg)
    }

    fun insertFavrtImg(favImg: FavModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFavouriteImg(favImg)
    }

    fun insertSVImg(SavImg: SavedModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertSavedImg(SavImg)
    }

}