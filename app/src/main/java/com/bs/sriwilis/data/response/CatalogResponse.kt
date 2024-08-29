package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class CatalogResponse(

	@field:SerializedName("data")
	val data: List<CatalogData>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CatalogData(

	@field:SerializedName("judul_katalog")
	val judulKatalog: String? = null,

	@field:SerializedName("deskripsi_katalog")
	val deskripsiKatalog: String? = null,

	@field:SerializedName("harga_katalog")
	val hargaKatalog: Int? = null,

	@field:SerializedName("no_wa")
	val noWa: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("shopee_link")
	val shopeeLink: String? = null,

	@field:SerializedName("gambar_katalog")
	val gambarKatalog: String? = null
)
