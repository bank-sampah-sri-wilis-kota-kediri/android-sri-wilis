package com.bangkit.factha.data.network

import com.bs.sriwilis.data.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServiceAuth {
    @FormUrlEncoded
    @POST("admin/login")
    suspend fun login(
        @Field("no_hp_admin") no_hp_admin: String,
        @Field("password_admin") password_admin: String
    ): Response<LoginResponse>
}