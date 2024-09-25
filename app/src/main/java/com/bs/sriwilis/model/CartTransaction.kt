package com.bs.sriwilis.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartTransaction(
    val id: String,
    val id_keranjang_transaksi: String,
    val kategori: String,
    val berat: Int,
    val harga: Float,
    val gambar: String?
) : Parcelable

