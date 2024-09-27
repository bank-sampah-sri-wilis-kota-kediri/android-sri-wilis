package com.bs.sriwilis.data.response

import com.bs.sriwilis.model.CartTransaction

data class CartTransactionRequest(
    val id_nasabah: String,
    val tanggal: String,
    val cartTransaction: List<CartTransaction>
)