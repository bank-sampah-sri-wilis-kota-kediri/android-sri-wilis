package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class AdminResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SingleAdminResponse(
	@field:SerializedName("data")
	val data: AdminData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AdminData(

	@field:SerializedName("jenis_admin")
	val jenisAdmin: String? = null,

	@field:SerializedName("alamat_admin")
	val alamatAdmin: String? = null,

	@field:SerializedName("gambar_admin")
	val gambarAdmin: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("nama_admin")
	val namaAdmin: String? = null,

	@field:SerializedName("no_hp_admin")
	val noHpAdmin: String? = null
)
