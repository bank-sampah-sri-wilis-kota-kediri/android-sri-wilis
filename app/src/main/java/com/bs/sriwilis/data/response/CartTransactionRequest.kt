package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class CartTransactionRequest(
    @SerializedName("transaksi_sampah")
    val transaksiSampah: List<TransaksiSampahItem>,

    @SerializedName("id_nasabah")
    val idNasabah: String,

    @SerializedName("tanggal")
    val tanggal: String
)
