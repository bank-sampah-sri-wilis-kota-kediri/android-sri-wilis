package com.bs.sriwilis.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bs.sriwilis.data.repository.modelhelper.CardCategory
import com.bs.sriwilis.data.room.entity.CategoryEntity
import com.bs.sriwilis.data.room.entity.NasabahEntity
import com.bs.sriwilis.data.room.entity.PesananSampahEntity
import com.bs.sriwilis.data.room.entity.PesananSampahKeranjangEntity

@Dao
interface PesananSampahDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pesananSampah: List<PesananSampahEntity>)

    @Query("SELECT * FROM pesanan_sampah_table")
    suspend fun getAllPesananSampah(): List<PesananSampahEntity>

    @Query("DELETE FROM pesanan_sampah_table")
    suspend fun deleteAll()

    @Query("SELECT berat_perkiraan FROM pesanan_sampah_table WHERE id_pesanan_sampah_keranjang = :idKeranjang")
    suspend fun getBeratPerkiraanById(idKeranjang: String): String?
}