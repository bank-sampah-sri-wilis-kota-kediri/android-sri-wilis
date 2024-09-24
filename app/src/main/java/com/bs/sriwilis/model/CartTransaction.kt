package com.bs.sriwilis.model

data class CartTransaction(
    val id: String,
    val id_keranjang_transaksi: String,
    val kategori: String,
    val berat: Int,
    val harga: Float,
    val gambar: String
)

