package com.pebbletechsolutions.memegenrator.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavAndSavedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavImg(imgUrl: FavModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSavedImg(savImgUri: SavedModel)

    @Delete
    suspend fun deleteFavImg(imgUrl: FavModel)

    @Query("Select * FROM favourite order by id ASC")
    fun getAllFavImg(): LiveData<List<FavModel>>


    @Query("Select * FROM SavedImgList order by id ASC")
    fun getAllSavedImg(): LiveData<List<SavedModel>>




}