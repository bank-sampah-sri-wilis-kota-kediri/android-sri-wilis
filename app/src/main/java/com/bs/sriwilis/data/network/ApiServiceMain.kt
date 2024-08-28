package com.bs.sriwilis.data.network

import com.bs.sriwilis.data.response.AddCategoryRequest
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.GetUserByIdResponse
import com.bs.sriwilis.data.response.LoginResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceMain {


    // ADMIN

    // USER CRUD
    @GET("nasabah/show-all")
    suspend fun getAllUser(
        @Header("X-Auth-Token") token: String,
    ): Response<GetAllUserResponse>

    @GET("nasabah/{id}")
    suspend fun getUserById(
        @Path("id") userId: String,
        @Header("X-Auth-Token") token: String
    ): Response<GetUserByIdResponse>

    @FormUrlEncoded
    @POST("nasabah/add-nasabah")
    suspend fun registerUser(
        @Header("X-Auth-Token") token: String,
        @Field("no_hp_nasabah") no_hp_nasabah: String,
        @Field("password_nasabah") password_nasabah: String,
        @Field("nama_nasabah") nama_nasabah: String,
        @Field("alamat_nasabah") alamat_nasabah: String,
        @Field("saldo_nasabah") saldo_nasabah: String
        ): Response<RegisterUserResponse>

    @FormUrlEncoded
    @PUT("nasabah/edit-by-id/{id}")
    suspend fun editUser(
        @Path("id") userId: String,
        @Header("X-Auth-Token") token: String,
        @Field("no_hp_nasabah") no_hp_nasabah: String,
        @Field("nama_nasabah") nama_nasabah: String,
        @Field("alamat_nasabah") alamat_nasabah: String,
        @Field("saldo_nasabah") saldo_nasabah: Double
    ): Response<RegisterUserResponse>

    @DELETE("nasabah/{id}")
    suspend fun deleteUser(
        @Path("id") id: String,
        @Header("X-Auth-Token") token: String
    ): Response<Unit>

    // KATEGORI CRUD
    @POST("kategori/")
    suspend fun addCategory(
        @Header("X-Auth-Token") token: String,
        @Body requestBody: AddCategoryRequest
    ): Response<CategoryResponse>




}