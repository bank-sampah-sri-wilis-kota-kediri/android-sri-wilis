package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("data")
	val data: List<CategoryData?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CategoryData(

	@field:SerializedName("jenis_kategori")
	val jenisKategori: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("harga_kategori")
	val hargaKategori: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("gambar_kategori")
	val gambarKategori: String? = null,

	@field:SerializedName("nama_kategori")
	val namaKategori: String? = null
)
