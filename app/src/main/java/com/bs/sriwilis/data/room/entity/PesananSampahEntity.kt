package com.bs.sriwilis.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pesanan_sampah_table",
    foreignKeys = [ForeignKey(
        entity = PesananSampahKeranjangEntity::class,
        parentColumns = ["id_pesanan"],
        childColumns = ["id_pesanan_sampah_keranjang"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PesananSampahEntity(
    @PrimaryKey val id: String,
    val id_pesanan_sampah_keranjang: String,
    val kategori: String,
    val berat_perkiraan: String,
    val harga_perkiraan: String?,
    val gambar: String?,
    val created_at: String?,
    val updated_at: String?,
)