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
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.room.TransaksiSampahEntity

@Dao
interface PesananSampahDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pesananSampah: PesananSampahEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pesananSampah: List<PesananSampahEntity>)

    @Query("""
    SELECT id_pesanan_sampah_keranjang, kategori as nama_kategori, berat_perkiraan as berat, harga_perkiraan as harga
    FROM pesanan_sampah_table
    WHERE id_pesanan_sampah_keranjang = :idPesanan
    """)
    suspend fun getPesananSampahKeranjangDetailList(idPesanan: String): List<CardDetailPesanan>

    @Query("SELECT * FROM pesanan_sampah_table")
    suspend fun getAllPesananSampah(): List<PesananSampahEntity>

    @Query("DELETE FROM pesanan_sampah_table")
    suspend fun deleteAll()

    @Query("SELECT berat_perkiraan FROM pesanan_sampah_table WHERE id_pesanan_sampah_keranjang = :idKeranjang")
    suspend fun getBeratPerkiraanById(idKeranjang: String): String?
}