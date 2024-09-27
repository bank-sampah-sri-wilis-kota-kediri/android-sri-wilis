package com.bs.sriwilis.data.network

import android.provider.ContactsContract.Data
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.AddCatalogRequest
import com.bs.sriwilis.data.response.AdminResponse
import com.bs.sriwilis.data.response.CartTransactionRequest
import com.bs.sriwilis.data.response.CatalogResponse
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.CategoryResponseDTO
import com.bs.sriwilis.data.response.DataKeranjangItem
import com.bs.sriwilis.data.response.DataKeranjangItemResponse
import com.bs.sriwilis.data.response.GetAdminByIdResponse
import com.bs.sriwilis.data.response.GetCatalogByIdResponse
import com.bs.sriwilis.data.response.GetCategoryByIdResponse
import com.bs.sriwilis.data.response.GetUserByIdResponse
import com.bs.sriwilis.data.response.NasabahResponseDTO
import com.bs.sriwilis.data.response.OrderCartResponse
import com.bs.sriwilis.data.response.PesananSampahItem
import com.bs.sriwilis.data.response.PesananSampahKeranjangResponse
import com.bs.sriwilis.data.response.PesanananSampahItemResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import com.bs.sriwilis.data.response.SingleAdminResponse
import com.bs.sriwilis.data.response.SingleCatalogResponse
import com.bs.sriwilis.data.response.SingleCategoryResponse
import com.bs.sriwilis.data.response.SinglePesananSampahResponse
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransactionResponse
import com.bs.sriwilis.data.response.TransaksiSampahItem
import com.bs.sriwilis.model.CartTransaction
import retrofit2.Call
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
        @Header("Authorization") token: String,
    ): Response<GetAdminByIdResponse>

    @FormUrlEncoded
    @PUT("admin/edit-by-id/{id}")
    suspend fun editAdmin(
        @Path("id") adminId: String,
        @Header("Authorization") token: String,
        @Field("nama_admin") nama_admin: String,
        @Field("no_hp_admin") no_hp_admin: String,
        @Field("alamat_admin") alamat_admin: String,
        @Field("gambar_admin") gambar_admin: String,
    ): Response<SingleAdminResponse>

    @FormUrlEncoded
    @PUT("admin/edit-password-by-id/{id}")
    suspend fun changePasswordAdmin(
        @Path("id") adminId: String,
        @Header("Authorization") token: String,
        @Field("password_admin") password_admin: String,
    ): Response<AdminResponse>

    // USER CRUD
    @GET("nasabah/show-all")
    suspend fun getAllNasabah(
        @Header("Authorization") token: String,
    ): Response<NasabahResponseDTO>

    @GET("nasabah/{no_hp_admin}")
    suspend fun getUserById(
        @Path("no_hp_admin") userId: String,
        @Header("Authorization") token: String
    ): Response<GetUserByIdResponse>

    @FormUrlEncoded
    @POST("nasabah/add-nasabah")
    suspend fun registerUser(
        @Header("Authorization") token: String,
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
        @Header("Authorization") token: String,
        @Field("nama_nasabah") nama_nasabah: String,
        @Field("no_hp_nasabah") no_hp_nasabah: String,
        @Field("alamat_nasabah") alamat_nasabah: String,
        @Field("gambar_nasabah") gambar_nasabah: String,
    ): Response<GetUserByIdResponse>

    @DELETE("nasabah/{no_hp_nasabah}")
    suspend fun deleteUser(
        @Path("no_hp_nasabah") id: String,
        @Header("Authorization") token: String
    ): Response<Unit>

    // KATEGORI CRUD
    @FormUrlEncoded
    @POST("kategori/add")
    suspend fun addCategory(
        @Header("Authorization") token: String,
        @Field("nama_kategori") nama_kategori: String,
        @Field("harga_kategori") harga_kategori: String,
        @Field("jenis_kategori") jenis_kategori: String,
        @Field("gambar_kategori") gambar_kategori: String,
    ): Response<CategoryResponse>

    @GET("kategori/show-all")
    suspend fun getAllCategory(
        @Header("Authorization") token: String,
    ): Response<CategoryResponseDTO>

    @GET("kategori/{id}")
    suspend fun getCategoryById(
        @Path("id") categoryId: String,
        @Header("Authorization") token: String
    ): Response<GetCategoryByIdResponse>

    @FormUrlEncoded
    @PUT("kategori/edit-by-id/{id}")
    suspend fun editCategory(
        @Path("id") categoryId: String,
        @Header("Authorization") token: String,
        @Field("nama_kategori") nama_kategori: String,
        @Field("harga_kategori") harga_kategori: String,
        @Field("jenis_kategori") jenis_kategori: String,
        @Field("gambar_kategori") gambar_kategori: String
    ): Response<SingleCategoryResponse>

    @DELETE("kategori/{id}")
    suspend fun deleteCategory(
        @Path("id") categoryId: String,
        @Header("Authorization") token: String
    ): Response<Unit>

    // KATALOG CRUD
    @FormUrlEncoded
    @POST("katalog/add")
    suspend fun addCatalog(
        @Header("Authorization") token: String,
        @Field("judul_katalog") judul_katalog: String,
        @Field("deskripsi_katalog") deskripsi_katalog: String,
        @Field("harga_katalog") harga_katalog: String,
        @Field("no_wa") no_wa: String,
        @Field("shopee_link") shopee_link: String,
        @Field("gambar_katalog") gambar_katalog: String,
    ): Response<SingleCatalogResponse>

    @GET("katalog/show-all")
    suspend fun getAllCatalog(
        @Header("Authorization") token: String,
    ): Response<CatalogResponse>

    @GET("katalog/{id}")
    suspend fun getCatalogById(
        @Path("id") catalogId: String,
        @Header("Authorization") token: String
    ): Response<GetCatalogByIdResponse>

    @FormUrlEncoded
    @PUT("katalog/edit-by-id/{id}")
    suspend fun editCatalog(
        @Path("id") catalogId: String,
        @Header("Authorization") token: String,
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
        @Header("Authorization") token: String
    ): Response<Unit>

    // PENARIKAN CRUD
    @POST("penarikan/add")
    suspend fun addMutation(
        @Header("Authorization") token: String,
        @Body requestBody: AddCatalogRequest
    ): Response<CatalogResponse>

    @GET("penarikan/show-all")
    suspend fun getAllMutation(
        @Header("Authorization") token: String,
    ): Response<CatalogResponse>

    @GET("penarikan/{id}")
    suspend fun getMutationById(
        @Path("id") catalogId: String,
        @Header("Authorization") token: String
    ): Response<GetCatalogByIdResponse>

    @PUT("penarikan/edit-by-id/{id}")
    suspend fun updateMutationStatus(
        @Path("id") catalogId: String,
        @Header("Authorization") token: String,
        @Body requestBody: AddCatalogRequest
    ): Response<CatalogResponse>

    @DELETE("penarikan/{id}")
    suspend fun deleteMutation(
        @Path("id") categoryId: String,
        @Header("Authorization") token: String
    ): Response<Unit>

    // TRANSACTION CRUD

    @POST("transaksi/add")
    suspend fun addCartTransaction(
        @Header("Authorization") token: String,
        @Body cartTransactionRequest: CartTransactionRequest
    ): Response<TransactionResponse>

    @GET("transaksi/show-all")
    suspend fun getAllTransaction(
        @Header("Authorization") token: String,
    ): Response<TransactionResponse>

    // SCHEDULING CRUD

    @GET("transaksi/nasabah/{id}")
    suspend fun getOrderCartByNasabahId(
        @Header("Authorization") token: String,
        @Path("id") id_nasabah: String,
        ): Response<OrderCartResponse>

    @GET("transaksi/nasabah/{id}")
    suspend fun getTransactionDetailByNasabahId(
        @Header("Authorization") token: String,
        @Path("id") id_nasabah: String,
    ): Response<PesanananSampahItemResponse>

    @GET("transaksi/nasabah/{id}")
    suspend fun getDataKeranjangDetailByNasabahId(
        @Header("Authorization") token: String,
        @Path("id") id_nasabah: String,
    ): Response<DataKeranjangItemResponse>


    @GET("pesanan/show-all-pesanan-sampah-keranjang")
    suspend fun getAllOrderSchedule(
        @Header("Authorization") token: String,
    ): Response<PesananSampahKeranjangResponse>

    @FormUrlEncoded
    @PUT("pesanan/update-tanggal-penjemputan/{id}")
    suspend fun registerDate(
        @Path("id") orderId: String,
        @Header("Authorization") token: String,
        @Field("tanggal") tanggal: String,
    ): Response<SinglePesananSampahResponse>

    @FormUrlEncoded
    @PUT("pesanan/update-status/{id}")
    suspend fun updateStatusSelesai(
        @Path("id") orderId: String,
        @Header("Authorization") token: String,
        @Field("dummyField") dummyField: String = "1"
    ): Response<SinglePesananSampahResponse>

    @FormUrlEncoded
    @PUT("pesanan/update-status-gagal/{id}")
    suspend fun updateStatusGagal(
        @Path("id") orderId: String,
        @Header("Authorization") token: String,
        @Field("dummyField") dummyField: String = "1"
    ): Response<SinglePesananSampahResponse>

    @GET("pesanan/selesai-diantar/{id}")
    suspend fun getOrderScheduleById(
        @Header("Authorization") token: String,
    ): Response<PesananSampahKeranjangResponse>
}