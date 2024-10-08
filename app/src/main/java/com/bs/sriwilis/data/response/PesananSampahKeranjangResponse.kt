package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class PesananSampahKeranjangResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("data_keranjang")
	var dataKeranjang: List<DataKeranjangItem>? = null
)

data class PesananSampahItem(

	@field:SerializedName("berat_perkiraan")
	val beratPerkiraan: Int? = null,

	@field:SerializedName("harga_perkiraan")
	val hargaPerkiraan: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("id_pesanan_sampah_keranjang")
	val idPesananSampahKeranjang: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("gambar")
	val gambar: String? = null
)

data class SinglePesananSampahResponse(
	@field:SerializedName("data")
	val data: DataKeranjangItem? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ChangeResultResponse(
	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataKeranjangItemResponse(
	val status: String,
	val data: List<DataKeranjangItem>
)

data class DataKeranjangItem(

	@field:SerializedName("nominal_transaksi")
	val nominalTransaksi: String? = null,

	@field:SerializedName("id_pesanan")
	val idPesanan: String? = null,

	@field:SerializedName("id_nasabah")
	val idNasabah: String,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("id_petugas")
	val idPetugas: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("pesanan_sampah")
	val pesananSampah: List<PesananSampahItem?>? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("long")
	val jsonMemberLong: String? = null,

	@field:SerializedName("status_pesanan")
	val statusPesanan: String? = null
)
