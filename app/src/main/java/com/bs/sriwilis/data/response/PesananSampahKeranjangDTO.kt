package com.bs.sriwilis.data.response

data class PesananSampahKeranjangDTO (
    val data: List<PesananSampahKeranjangData>?,
    val success: Boolean?,
    val message: String?
)

data class PesananSampahKeranjangData(
    val id: String,
    val id_pesanan: String,
    val id_nasabah: String,
    val id_petugas: String,
    val nominal_transaksi: String?,
    val tanggal: String?,
    val lat: String?,
    val long: String?,
    val status_pesanan: String?,
)