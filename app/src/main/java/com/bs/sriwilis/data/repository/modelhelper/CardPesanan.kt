package com.bs.sriwilispetugas.data.repository.modelhelper

data class CardPesanan(
    val id_nasabah: String,
    val id_pesanan: String,
    val id_keranjang_transaksi: String,
    val nominal_transaksi: String,
    val tanggal: String,
    val lat: String,
    val lng: String,
    val status_pesanan: String,
    val alamat_nasabah: String,
    val nama_nasabah: String,
    val no_hp_nasabah: String,
    val total_berat: Double
)

data class CardTransaksi(
    val id: String,
    val id_keranjang_transaksi: String,
    val nominal_transaksi: String,
    val tanggal: String,
    val alamat_nasabah: String,
    val nama_nasabah: String,
    val no_hp_nasabah: String,
    val total_berat: Double,
    val status_transaksi: String
)

data class CardStatus(
    val status_transaksi: String
)