package com.bs.sriwilispetugas.data.repository.modelhelper

data class CardDetailPesanan(
    val id: String,
    val id_pesanan_sampah_keranjang: String,
    val nama_kategori: String,
    var berat: Float,
    val gambar: String?,
    var harga: Float,
    val harga_kategori: Float
)