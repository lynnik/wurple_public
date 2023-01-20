package com.wurple.data.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wurple.data.model.preview.PreviewStorageData
import com.wurple.data.model.user.UserStorageData
import com.wurple.data.storage.room.dao.PreviewDao
import com.wurple.data.storage.room.dao.UserDao

@Database(
    version = 2,
    entities = [
        UserStorageData::class,
        PreviewStorageData::class
    ]
)
abstract class WurpleDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun previewDao(): PreviewDao

    companion object {
        private const val DATABASE_NAME = "wurple-database"
        fun provide(context: Context): WurpleDatabase {
            return Room.databaseBuilder(context, WurpleDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}