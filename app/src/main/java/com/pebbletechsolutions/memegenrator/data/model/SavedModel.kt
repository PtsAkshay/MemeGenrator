package com.pebbletechsolutions.memegenrator.data.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedImgList")
class SavedModel(@ColumnInfo(name = "SavedImgUri")val SavImgUri: String) {
    @PrimaryKey(autoGenerate = true) var id = 0
}