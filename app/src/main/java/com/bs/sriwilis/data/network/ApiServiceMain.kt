package com.bs.sriwilis.data.network

import com.bs.sriwilis.data.response.AddCategoryRequest
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.LoginResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiServiceMain {


    // ADMIN

    // USER CRUD
    @GET("nasabah/show-all")
    suspend fun getAllNews(
        @Header("X-Auth-Token") token: String,
    ): Response<GetAllUserResponse>

    @FormUrlEncoded
    @POST("nasabah/add-nasabah")
    suspend fun registerUser(
        @Field("no_hp_nasabah") no_hp_nasabah: String,
        @Field("password_nasabah") password_nasabah: String,
        @Field("nama_nasabah") nama_nasabah: String,
        @Field("alamat_nasabah") alamat_nasabah: String,
        @Field("saldo_nasabah") saldo_nasabah: String
        ): Response<RegisterUserResponse>

    @FormUrlEncoded
    @PUT("admin/edit")
    suspend fun editUser(
        @Field("no_hp_nasabah") no_hp_nasabah: String,
        @Field("password_nasabah") password_nasabah: String,
        @Field("nama_nasabah") nama_nasabah: String,
        @Field("alamat_nasabah") alamat_nasabah: String,
        @Field("saldo_nasabah") saldo_nasabah: String
    ): Response<RegisterUserResponse>

    @POST("kategori/")
    suspend fun addCategory(
        @Header("X-Auth-Token") token: String,
        @Body requestBody: AddCategoryRequest
    ): Response<CategoryResponse>




}