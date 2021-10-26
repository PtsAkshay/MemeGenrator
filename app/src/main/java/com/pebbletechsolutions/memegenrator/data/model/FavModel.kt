package com.pebbletechsolutions.memegenrator.data.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
class FavModel(@ColumnInfo(name = "ImgUri") val FavImgUri: String){
    @PrimaryKey(autoGenerate = true) var id = 0
}
