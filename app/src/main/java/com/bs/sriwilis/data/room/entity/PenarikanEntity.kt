package com.bs.sriwilis.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "penarikan_table")
data class PenarikanEntity(
    @PrimaryKey
    val id: Int,
    val id_nasabah: Int,
    val jenis_penarikan: String,
    val nominal: Long,
    val tanggal: String,
    val nomor_meteran: Long?,
    val nomor_token: String?,
    val nomor_rekening: Long?,
    val jenis_bank: String?,
    val status_penarikan: String
)