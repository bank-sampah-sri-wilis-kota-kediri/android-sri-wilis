package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class PenarikanResponse(

	@field:SerializedName("data")
	val data: PenarikanData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PenarikanData(

	@field:SerializedName("jenis_bank")
	val jenisBank: Any? = null,

	@field:SerializedName("nomor_meteran")
	val nomorMeteran: Long? = null,

	@field:SerializedName("jenis_penarikan")
	val jenisPenarikan: String? = null,

	@field:SerializedName("id_nasabah")
	val idNasabah: Int? = null,

	@field:SerializedName("nominal")
	val nominal: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("nomor_rekening")
	val nomorRekening: Any? = null,

	@field:SerializedName("status_penarikan")
	val statusPenarikan: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("id")
	val id: String
)

data class PenarikanListResponse(

	@field:SerializedName("data")
	val data: List<PenarikanData>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
