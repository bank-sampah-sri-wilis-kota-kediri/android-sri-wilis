package com.bs.sriwilis.data.response

import java.util.Date

data class PenarikanResponseDTO(
    val success: Boolean?,
    val message: String?,
    val data: List<Penarikan>?
)

data class Penarikan(
    val id: Int,
    val id_nasabah: Int,
    val jenis_penarikan: String,
    val nominal: Long,
    val tanggal: String,
    val nomor_meteran: Long?,
    val nomor_token: Long?,
    val nomor_rekening: Long?,
    val jenis_bank: String?,
    val status_penarikan: String
)