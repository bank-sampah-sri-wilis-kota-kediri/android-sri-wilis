package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class NasabahResponseDTO (
    val data: List<UserItem>?,
    val success: Boolean?,
    val message: String?
)


data class UserItem(
    val no_hp_nasabah: String?,
    val jasa: String?,
    val nama_nasabah: String?,
    val is_dapat_jasa: String?,
    val alamat_nasabah: String?,
    val saldo_nasabah: String?,
    val id: Int,
    val gambar_nasabah: String? = null
)