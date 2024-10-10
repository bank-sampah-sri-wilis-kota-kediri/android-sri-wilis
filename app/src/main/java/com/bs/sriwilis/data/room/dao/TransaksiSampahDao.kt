package com.bs.sriwilispetugas.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.room.TransaksiSampahEntity

@Dao
interface TransaksiSampahDao {

    // Insert a single TransaksiSampahEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksiSampah(pesananSampah: TransaksiSampahEntity)

    // Insert multiple TransaksiSampahEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTransaksiSampah(pesananSampahList: List<TransaksiSampahEntity>)

    @Query("""
    SELECT id_keranjang_transaksi, kategori as nama_kategori, berat as berat, harga as harga
    FROM transaksi_sampah_table
    WHERE id_keranjang_transaksi = :idPesanan
    """)
    suspend fun getTransaksiSampahKeranjangDetailList(idPesanan: String): List<CardDetailPesanan>

    // Update a TransaksiSampah
    @Update
    suspend fun updateTransaksiSampah(pesananSampah: TransaksiSampahEntity)

    @Query("DELETE FROM transaksi_sampah_table")
    suspend fun deleteAllTransaksiSampah()

    // Delete all TransaksiSampah records for a specific DataKeranjang
    @Query("DELETE FROM transaksi_sampah_table WHERE id_keranjang_transaksi = :idPesananKeranjang")
    suspend fun deleteTransaksiSampahByKeranjangId(idPesananKeranjang: String)
}