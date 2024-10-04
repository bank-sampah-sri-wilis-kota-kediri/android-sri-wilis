package com.bs.sriwilis.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.bs.sriwilis.data.room.dao.CatalogDao
import com.bs.sriwilis.data.room.dao.CategoryDao
import com.bs.sriwilis.data.room.dao.KeranjangTransaksiDao
import com.bs.sriwilis.data.room.dao.LoginResponseDao
import com.bs.sriwilis.data.room.dao.NasabahDao
import com.bs.sriwilis.data.room.dao.PesananSampahDao
import com.bs.sriwilis.data.room.dao.PesananSampahKeranjangDao
import com.bs.sriwilis.data.room.entity.CatalogEntity
import com.bs.sriwilis.data.room.entity.CategoryEntity
import com.bs.sriwilis.data.room.entity.LoginResponseEntity
import com.bs.sriwilis.data.room.entity.NasabahEntity
import com.bs.sriwilis.data.room.entity.PesananSampahEntity
import com.bs.sriwilis.data.room.entity.PesananSampahKeranjangEntity
import com.bs.sriwilispetugas.data.room.KeranjangTransaksiEntity
import com.bs.sriwilispetugas.data.room.TransaksiSampahEntity
import com.bs.sriwilispetugas.data.room.dao.TransaksiSampahDao

@Database(
    entities = [LoginResponseEntity::class, NasabahEntity::class, CategoryEntity::class, CatalogEntity::class, PesananSampahKeranjangEntity::class, PesananSampahEntity::class, TransaksiSampahEntity::class, KeranjangTransaksiEntity::class],
    version = 12, // Update the version when modifying database structure
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loginResponseDao(): LoginResponseDao
    abstract fun nasabahDao(): NasabahDao
    abstract fun categoryDao(): CategoryDao
    abstract fun catalogDao(): CatalogDao
    abstract fun pesananSampahKeranjangDao(): PesananSampahKeranjangDao
    abstract fun pesananSampahDao(): PesananSampahDao
    abstract fun transaksiSampahDao(): TransaksiSampahDao
    abstract fun keranjangTransaksiDao(): KeranjangTransaksiDao

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