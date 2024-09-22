package com.bs.sriwilis.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.bs.sriwilis.data.room.dao.LoginResponseDao
import com.bs.sriwilis.data.room.dao.NasabahDao
import com.bs.sriwilis.data.room.entity.LoginResponseEntity
import com.bs.sriwilis.data.room.entity.NasabahEntity

@Database(
    entities = [LoginResponseEntity::class, NasabahEntity::class],
    version = 4, // Update the version when modifying database structure
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loginResponseDao(): LoginResponseDao
    abstract fun nasabahDao(): NasabahDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}