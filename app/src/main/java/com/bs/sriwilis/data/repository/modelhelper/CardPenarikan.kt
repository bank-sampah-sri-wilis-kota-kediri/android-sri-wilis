package com.bs.sriwilis.data.repository.modelhelper

data class CardPenarikan(
    val id: String,
    val id_nasabah: String,
    val jenis_penarikan: String,
    val nominal: String,
    val tanggal: String,
    val nomor_meteran: String?,
    val nomor_token: Long?,
    val nomor_rekening: String?,
    val jenis_bank: String?,
    val status_penarikan: String
)