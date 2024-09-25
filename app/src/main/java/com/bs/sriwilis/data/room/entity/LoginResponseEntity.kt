package com.bs.sriwilis.data.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "login_response_table")
data class LoginResponseEntity(
    @PrimaryKey val id: Int,
    val success: Boolean?,
    val message: String?,
    val access_token: String?,
    val nama_admin: String?,
    val no_hp_admin: String?,
    val alamat_admin: String?,
    val jenis_admin: String?,
    val password_admin: String?,
    val gambar_admin: String?
)
