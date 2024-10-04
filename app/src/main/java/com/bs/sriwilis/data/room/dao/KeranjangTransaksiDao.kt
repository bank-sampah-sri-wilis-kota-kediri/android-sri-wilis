package com.bs.sriwilis.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardStatus
import com.bs.sriwilispetugas.data.repository.modelhelper.CardTransaksi
import com.bs.sriwilispetugas.data.room.KeranjangTransaksiEntity

@Dao
interface KeranjangTransaksiDao {

    // Insert a single KeranjangTransaksiEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeranjangTransaksi(keranjangTransaksi: KeranjangTransaksiEntity): Long

    // Insert multiple KeranjangTransaksiEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeranjangTransaksi(keranjangTransaksiList: List<KeranjangTransaksiEntity>)

    // Get all status_transaksi from the table
    @Query("SELECT status_transaksi FROM keranjang_transaksi_table")
    suspend fun getStatusKeranjangTransaksi(): CardStatus

    // Get all staKeranjangTransaksi from the table
    @Query("SELECT * FROM keranjang_transaksi_table")
    suspend fun getAllKeranjangTransaksi(): List<KeranjangTransaksiEntity>

    // Get a single KeranjangTransaksi by its ID
    @Query("""    
        SELECT n.nama_nasabah, 
       n.no_hp_nasabah as no_hp_nasabah, 
       p.nominal_transaksi, 
       p.tanggal, 
       p.id,
       n.alamat_nasabah, 
       SUM(ts.berat) AS total_berat
        FROM keranjang_transaksi_table AS p
        JOIN nasabah_table AS n ON p.id_nasabah = n.id
        JOIN transaksi_sampah_table as ts ON p.id = ts.id_keranjang_transaksi
        WHERE p.id = :idPesanan
        GROUP BY n.id, p.nominal_transaksi, p.tanggal
        """)
    suspend fun getDataDetailKeranjangTransaksi(idPesanan: String): CardTransaksi

    // Get List data detail card pesanan
    @Query("""
    SELECT kategori as nama_kategori, berat as berat
    FROM transaksi_sampah_table
    WHERE id_keranjang_transaksi = :idPesanan
    """)
    suspend fun getTransaksiSampah(idPesanan: String): List<CardDetailPesanan>

    // Update a KeranjangTransaksi
    @Update
    suspend fun updateKeranjangTransaksi(keranjangTransaksi: KeranjangTransaksiEntity)

    // Delete a KeranjangTransaksi
    @Delete
    suspend fun deleteKeranjangTransaksi(keranjangTransaksi: KeranjangTransaksiEntity)

    // Delete all KeranjangTransaksi records
    @Query("DELETE FROM keranjang_transaksi_table")
    suspend fun deleteAllKeranjangTransaksi()

    @Query("""    
        SELECT n.nama_nasabah, 
       n.no_hp_nasabah as no_hp_nasabah, 
       p.nominal_transaksi, 
       p.tanggal, 
       p.id,
       n.alamat_nasabah,
       SUM(ts.berat) AS total_berat
        FROM keranjang_transaksi_table AS p
        JOIN nasabah_table AS n ON p.id_nasabah = n.id
        JOIN transaksi_sampah_table as ts ON p.id = ts.id_keranjang_transaksi
        GROUP BY n.id, p.nominal_transaksi, p.tanggal
        """)
    suspend fun getCombinedTransaksiData(): List<CardTransaksi>
}