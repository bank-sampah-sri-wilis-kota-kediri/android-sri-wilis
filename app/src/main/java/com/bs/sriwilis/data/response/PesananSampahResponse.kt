package com.bs.sriwilis.data.response

data class PesananSampahResponseDTO(
    val success: Boolean,
    val data_keranjang: List<DataKeranjang>
)

data class DataKeranjang(
    val id: String,
    val id_pesanan: String,
    val id_nasabah: String,
    val id_petugas: String,
    val nominal_transaksi: String,
    val tanggal: String,
    val lat: String,
    val long: String,
    val status_pesanan: String,
    val created_at: String,
    val updated_at: String,
    val pesanan_sampah: List<PesananSampah>
)

data class PesananSampah(
    val id: String,
    val id_pesanan_sampah_keranjang: String,
    val kategori: String,
    val berat_perkiraan: String,
    val harga_perkiraan: String,
    val gambar: String?,
    val created_at: String,
    val updated_at: String
)
