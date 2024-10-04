package com.bs.sriwilis.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pesanan_sampah_keranjang_table")
data class PesananSampahKeranjangEntity(
    @PrimaryKey val id_pesanan: String,
    val id_nasabah: String,
    val id_petugas: String,
    val nominal_transaksi: String?,
    val tanggal: String?,
    val lat: String?,
    val lng: String,
    val status_pesanan: String?,
    val created_at: String?,
    val updated_at: String?,
)