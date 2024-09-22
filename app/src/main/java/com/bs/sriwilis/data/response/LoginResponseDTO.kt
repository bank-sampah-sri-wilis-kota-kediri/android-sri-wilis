package com.bs.sriwilis.data.response

data class LoginResponseDTO(
    val success: Boolean?,
    val message: String?,
    val data: DataAdmin?
)

data class DataAdmin(
    val access_token: String?,
    val nama_admin: String?,
    val no_hp_admin: String?,
    val alamat_admin: String,
    val jenis_admin: String,
    val password_admin: String,
    val gambar_admin: String,
)
