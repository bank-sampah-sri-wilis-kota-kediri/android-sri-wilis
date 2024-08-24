package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class RegisterUserResponse(

	@field:SerializedName("data")
	val data: UserData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserData(

	@field:SerializedName("no_hp_nasabah")
	val noHpNasabah: String? = null,

	@field:SerializedName("jasa")
	val jasa: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("nama_nasabah")
	val namaNasabah: String? = null,

	@field:SerializedName("is_dapat_jasa")
	val isDapatJasa: String? = null,

	@field:SerializedName("alamat_nasabah")
	val alamatNasabah: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("saldo_nasabah")
	val saldoNasabah: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
