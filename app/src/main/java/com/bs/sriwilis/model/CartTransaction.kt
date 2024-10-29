package com.bs.sriwilis.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartTransaction(
    val kategori: String,
    val berat: Float,
    val harga: Float?,
    val gambar: String?,
) : Parcelable

