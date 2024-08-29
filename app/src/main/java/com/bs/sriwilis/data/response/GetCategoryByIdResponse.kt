package com.bs.sriwilis.data.response

import com.google.gson.annotations.SerializedName

data class GetCategoryByIdResponse(
    @field:SerializedName("data")
    val data: CategoryData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)