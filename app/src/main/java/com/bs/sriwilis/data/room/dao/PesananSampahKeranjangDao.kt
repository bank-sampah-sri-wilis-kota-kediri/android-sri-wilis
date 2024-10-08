package com.bs.sriwilis.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bs.sriwilis.data.repository.modelhelper.CardCategory
import com.bs.sriwilis.data.room.entity.CategoryEntity
import com.bs.sriwilis.data.room.entity.NasabahEntity
import com.bs.sriwilis.data.room.entity.PesananSampahKeranjangEntity
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan

@Dao
interface PesananSampahKeranjangDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pesananSampahKeranjangEntity: List<PesananSampahKeranjangEntity>)

    @Query("SELECT * FROM pesanan_sampah_keranjang_table")
    suspend fun getAllPesananSampahKeranjang(): List<PesananSampahKeranjangEntity>

    @Query("DELETE FROM pesanan_sampah_keranjang_table")
    suspend fun deleteAll()

    @Query("""
    SELECT kategori as nama_kategori, berat_perkiraan as berat
    FROM pesanan_sampah_table
    WHERE id_pesanan_sampah_keranjang = :idPesanan
    """)
    suspend fun getPesananSampahKeranjangDetailList(idPesanan: String): List<CardDetailPesanan>

    @Query("""    
        SELECT n.nama_nasabah, 
       n.no_hp_nasabah as no_hp_nasabah, 
       p.nominal_transaksi, 
       p.tanggal, 
       p.lat, 
       p.lng, 
       p.id_pesanan,
       p.status_pesanan,
       n.alamat_nasabah, 
       SUM(ps.berat_perkiraan) AS total_berat
        FROM pesanan_sampah_keranjang_table AS p
        JOIN nasabah_table AS n ON p.id_nasabah = n.id
        JOIN pesanan_sampah_table AS ps ON p.id_pesanan = ps.id_pesanan_sampah_keranjang
        GROUP BY n.id, p.nominal_transaksi, p.tanggal, p.lat, p.lng, p.status_pesanan
        """)
    suspend fun getPesananSampahKeranjang(): List<CardPesanan>

    @Query("""    
        SELECT n.nama_nasabah, 
       n.no_hp_nasabah as no_hp_nasabah, 
       p.nominal_transaksi, 
       p.tanggal, 
       p.lat, 
       p.lng, 
       p.id_pesanan,
       p.status_pesanan,
       n.alamat_nasabah, 
       SUM(ps.berat_perkiraan) AS total_berat
        FROM pesanan_sampah_keranjang_table AS p
        JOIN nasabah_table AS n ON p.id_nasabah = n.id
        JOIN pesanan_sampah_table AS ps ON p.id_pesanan = ps.id_pesanan_sampah_keranjang
        WHERE p.id_pesanan = :idPesanan
        GROUP BY n.id, p.nominal_transaksi, p.tanggal, p.lat, p.lng, p.status_pesanan
        """)
    suspend fun getDataDetailPesananSampahKeranjang(idPesanan: String): CardPesanan
}