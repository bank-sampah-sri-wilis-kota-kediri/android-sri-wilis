package com.bs.sriwilis.data.network

import com.bs.sriwilis.data.response.AddCatalogRequest
import com.bs.sriwilis.data.response.AddCategoryRequest
import com.bs.sriwilis.data.response.CatalogResponse
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.GetCatalogByIdResponse
import com.bs.sriwilis.data.response.GetCategoryByIdResponse
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

    @GET("kategori/show-all")
    suspend fun getAllCategory(
        @Header("X-Auth-Token") token: String,
    ): Response<CategoryResponse>

    @GET("kategori/{id}")
    suspend fun getCategoryById(
        @Path("id") categoryId: String,
        @Header("X-Auth-Token") token: String
    ): Response<GetCategoryByIdResponse>

    @FormUrlEncoded
    @PUT("kategori/edit-by-id/{id}")
    suspend fun editCategory(
        @Path("id") categoryId: String,
        @Header("X-Auth-Token") token: String,
        @Field("nama_kategori") nama_kategori: String,
        @Field("harga_kategori") harga_kategori: String,
        @Field("jenis_kategori") jenis_kategori: String,
        @Field("gambar_kategori") gambar_kategori: String
    ): Response<CategoryResponse>

    @DELETE("kategori/{id}")
    suspend fun deleteCategory(
        @Path("id") categoryId: String,
        @Header("X-Auth-Token") token: String
    ): Response<Unit>

    // KATALOG CRUD
    @POST("katalog/")
    suspend fun addCatalog(
        @Header("X-Auth-Token") token: String,
        @Body requestBody: AddCatalogRequest
    ): Response<CatalogResponse>

    @GET("katalog/show-all")
    suspend fun getAllCatalog(
        @Header("X-Auth-Token") token: String,
    ): Response<CatalogResponse>

    @GET("katalog/{id}")
    suspend fun getCatalogById(
        @Path("id") categoryId: String,
        @Header("X-Auth-Token") token: String
    ): Response<GetCatalogByIdResponse>

    @FormUrlEncoded
    @PUT("katalog/edit-by-id/{id}")
    suspend fun editCatalog(
        @Path("id") categoryId: String,
        @Header("X-Auth-Token") token: String,
        @Body requestBody: AddCatalogRequest
    ): Response<CatalogResponse>

    @DELETE("katalog/{id}")
    suspend fun deleteCatalog(
        @Path("id") categoryId: String,
        @Header("X-Auth-Token") token: String
    ): Response<Unit>


}