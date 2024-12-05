package com.bangkit.spotlyze.data.local.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity

@Database(entities = [SkincareEntity::class], version = 1, exportSchema = false)
abstract class SkincareDatabase : RoomDatabase() {
    abstract fun skincareDao(): SkincareDao

    companion object {
        private var INSTANCE: SkincareDatabase? = null
        fun getInstance(context: Context): SkincareDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                SkincareDatabase::class.java,
                "Skincare.db"
            ).build()
        }
    }
}