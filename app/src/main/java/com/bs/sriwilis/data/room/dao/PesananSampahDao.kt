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
    SELECT 
        ps.id, 
        ps.id_pesanan_sampah_keranjang, 
        ps.kategori AS nama_kategori, 
        ps.berat_perkiraan AS berat, 
        ps.harga_perkiraan AS harga,
        k.harga_kategori AS harga_kategori
    FROM 
        pesanan_sampah_table ps
    JOIN 
        category_table k ON ps.kategori = k.nama_kategori
    WHERE 
        ps.id_pesanan_sampah_keranjang = :idPesanan
""")
    suspend fun getPesananSampahKeranjangDetailList(idPesanan: String): List<CardDetailPesanan>

    @Query("SELECT * FROM pesanan_sampah_table")
    suspend fun getAllPesananSampah(): List<PesananSampahEntity>

    @Query("DELETE FROM pesanan_sampah_table")
    suspend fun deleteAll()
}