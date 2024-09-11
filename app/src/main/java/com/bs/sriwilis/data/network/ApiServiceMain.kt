package com.bs.sriwilis.data.network

import com.bs.sriwilis.data.response.AddCatalogRequest
import com.bs.sriwilis.data.response.AddCategoryRequest
import com.bs.sriwilis.data.response.AdminResponse
import com.bs.sriwilis.data.response.CatalogResponse
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.GetAdminByIdResponse
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.GetCatalogByIdResponse
import com.bs.sriwilis.data.response.GetCategoryByIdResponse
import com.bs.sriwilis.data.response.GetUserByIdResponse
import com.bs.sriwilis.data.response.LoginResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import com.bs.sriwilis.data.response.SingleAdminResponse
import com.bs.sriwilis.data.response.SingleCatalogResponse
import com.bs.sriwilis.data.response.SingleCategoryResponse
import com.bs.sriwilis.data.response.SingleUserResponse
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

    // ADMIN CRUD
    @GET("admin/show")
    suspend fun getAdminData(
        @Header("X-Auth-Token") token: String,
    ): Response<GetAdminByIdResponse>

    @FormUrlEncoded
    @PUT("admin/edit-by-id/{id}")
    suspend fun editAdmin(
        @Path("id") adminId: String,
        @Header("X-Auth-Token") token: String,
        @Field("nama_admin") nama_admin: String,
        @Field("no_hp_admin") no_hp_admin: String,
        @Field("alamat_admin") alamat_admin: String,
        @Field("gambar_admin") gambar_admin: String,
    ): Response<SingleAdminResponse>

    @FormUrlEncoded
    @POST("admin/edit-by-id/{id)")
    suspend fun changePasswordAdmin(
        @Path("id") adminId: String,
        @Header("X-Auth-Token") token: String,
        @Field("password_admin") password_admin: String,
    ): Response<SingleAdminResponse>

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
        ): Response<SingleUserResponse>

    @FormUrlEncoded
    @PUT("nasabah/edit-by-id/{id}")
    suspend fun editUser(
        @Path("id") userId: String,
        @Header("X-Auth-Token") token: String,
        @Field("nama_nasabah") nama_nasabah: String,
        @Field("no_hp_nasabah") no_hp_nasabah: String,
        @Field("alamat_nasabah") alamat_nasabah: String,
        @Field("gambar_nasabah") gambar_nasabah: String,
    ): Response<SingleUserResponse>

    @DELETE("nasabah/{id}")
    suspend fun deleteUser(
        @Path("id") id: String,
        @Header("X-Auth-Token") token: String
    ): Response<Unit>

    // KATEGORI CRUD
    @FormUrlEncoded
    @POST("kategori/add")
    suspend fun addCategory(
        @Header("X-Auth-Token") token: String,
        @Field("nama_kategori") no_hp_nasabah: String,
        @Field("harga_kategori") harga_kategori: String,
        @Field("jenis_kategori") jenis_kategori: String,
        @Field("gambar_kategori") gambar_kategori: String,
    ): Response<SingleCategoryResponse>

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
    ): Response<SingleCategoryResponse>

    @DELETE("kategori/{id}")
    suspend fun deleteCategory(
        @Path("id") categoryId: String,
        @Header("X-Auth-Token") token: String
    ): Response<Unit>

    // KATALOG CRUD
    @FormUrlEncoded
    @POST("katalog/add")
    suspend fun addCatalog(
        @Header("X-Auth-Token") token: String,
        @Field("judul_katalog") judul_katalog: String,
        @Field("deskripsi_katalog") deskripsi_katalog: String,
        @Field("harga_katalog") harga_katalog: String,
        @Field("no_wa") no_wa: String,
        @Field("shopee_link") shopee_link: String,
        @Field("gambar_katalog") gambar_katalog: String,
    ): Response<SingleCatalogResponse>

    @GET("katalog/show-all")
    suspend fun getAllCatalog(
        @Header("X-Auth-Token") token: String,
    ): Response<CatalogResponse>

    @GET("katalog/{id}")
    suspend fun getCatalogById(
        @Path("id") catalogId: String,
        @Header("X-Auth-Token") token: String
    ): Response<GetCatalogByIdResponse>

    @FormUrlEncoded
    @PUT("katalog/edit-by-id/{id}")
    suspend fun editCatalog(
        @Path("id") catalogId: String,
        @Header("X-Auth-Token") token: String,
        @Field("judul_katalog") judul_katalog: String,
        @Field("deskripsi_katalog") deskripsi_katalog: String,
        @Field("harga_katalog") harga_katalog: String,
        @Field("no_wa") no_wa: String,
        @Field("shopee_link") shopee_link: String,
        @Field("gambar_katalog") gambar_katalog: String,
    ): Response<SingleCatalogResponse>

    @DELETE("katalog/{id}")
    suspend fun deleteCatalog(
        @Path("id") categoryId: String,
        @Header("X-Auth-Token") token: String
    ): Response<Unit>

    // PENARIKAN CRUD
    @POST("penarikan/add")
    suspend fun addMutation(
        @Header("X-Auth-Token") token: String,
        @Body requestBody: AddCatalogRequest
    ): Response<CatalogResponse>

    @GET("penarikan/show-all")
    suspend fun getAllMutation(
        @Header("X-Auth-Token") token: String,
    ): Response<CatalogResponse>

    @GET("penarikan/{id}")
    suspend fun getMutationById(
        @Path("id") catalogId: String,
        @Header("X-Auth-Token") token: String
    ): Response<GetCatalogByIdResponse>

    @PUT("penarikan/edit-by-id/{id}")
    suspend fun updateMutationStatus(
        @Path("id") catalogId: String,
        @Header("X-Auth-Token") token: String,
        @Body requestBody: AddCatalogRequest
    ): Response<CatalogResponse>

    @DELETE("penarikan/{id}")
    suspend fun deleteMutation(
        @Path("id") categoryId: String,
        @Header("X-Auth-Token") token: String
    ): Response<Unit>

    // SETTINGS CRUD
}