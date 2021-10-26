package com.pebbletechsolutions.memegenrator.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(FavModel::class, SavedModel::class), version = 1, exportSchema = false)
abstract class MemeFavAndSavedListsDatabase: RoomDatabase() {

    abstract fun getFavAndSavedDao(): FavAndSavedDao

    companion object{

        @Volatile
        private var INSTANCE: MemeFavAndSavedListsDatabase? = null

        fun getMemeDatabase(context: Context): MemeFavAndSavedListsDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemeFavAndSavedListsDatabase::class.java,
                    "MemeFavAndSavedListsDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}