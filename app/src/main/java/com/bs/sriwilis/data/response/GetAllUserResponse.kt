package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class GetAllUserResponse(

	@field:SerializedName("data")
	val data: List<UserItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserItem(

	@field:SerializedName("no_hp_nasabah")
	val noHpNasabah: String? = null,

	@field:SerializedName("jasa")
	val jasa: String? = null,

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
	val saldoNasabah: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("gambar_nasabah")
	val gambarNasabah: String? = null
)
