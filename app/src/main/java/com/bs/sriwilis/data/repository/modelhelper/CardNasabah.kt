package com.bs.sriwilis.data.repository.modelhelper

data class CardNasabah(
    val no_hp_nasabah: String,
    val jasa: String?,
    val nama_nasabah: String?,
    val is_dapat_jasa: String?,
    val alamat_nasabah: String?,
    val saldo_nasabah: String?,
    val id: String,
    val gambar_nasabah: String? = null
)