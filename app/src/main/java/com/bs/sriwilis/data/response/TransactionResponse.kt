package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(

	@field:SerializedName("data")
	val data: List<TransactionDataItem>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class OrderCartResponse(
	val status: String,
	val data: List<TransactionDataItem>
)

data class TransactionDataItem(

	@field:SerializedName("nominal_transaksi")
	val nominalTransaksi: String? = null,

	@field:SerializedName("transaksi_sampah")
	val transaksiSampah: List<TransaksiSampahItem?>? = null,

	@field:SerializedName("id_nasabah")
	val idNasabah: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("status_transaksi")
	val statusTransaksi: String? = null
)

data class PesanananSampahItemResponse(
	val status: String,
	val data: List<PesananSampahItem>
)

data class TransaksiSampahItemResponse(
	val status: String,
	val data: List<TransaksiSampahItem>
)


data class TransaksiSampahItem(

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id_keranjang_transaksi")
	val idKeranjangTransaksi: Int? = null,

	@field:SerializedName("berat")
	val berat: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("gambar")
	val gambar: Any? = null
)
