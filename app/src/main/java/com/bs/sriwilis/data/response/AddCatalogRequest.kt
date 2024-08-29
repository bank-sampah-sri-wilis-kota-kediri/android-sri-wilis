package com.bs.sriwilis.data.response

data class AddCatalogRequest(
    val judul_katalog: String,
    val deskripsi_katalog: String,
    val harga_katalog: String,
    val no_wa: String,
    val shopee_link: String,
    val gambar_katalog: String,
    )
