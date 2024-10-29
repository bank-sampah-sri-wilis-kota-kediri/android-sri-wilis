package com.bs.sriwilis.data.response

import androidx.room.PrimaryKey

data class PesananSampahDTO (
    val data: List<PesananSampahData>?,
    val success: Boolean?,
    val message: String?
)

data class PesananSampahData(
    val id: String,
    val id_pesanan_sampah_keranjang: String,
    val kategori: String,
    val berat_perkiraan: Float,
    val harga_perkiraan: String?,
    val gambar: String?,
)