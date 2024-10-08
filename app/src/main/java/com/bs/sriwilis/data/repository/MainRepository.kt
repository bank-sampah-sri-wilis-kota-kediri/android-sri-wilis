package com.bs.sriwilis.data.repository

import android.provider.ContactsContract.Data
import android.util.Log
import com.bs.sriwilis.data.AppDatabase
import com.bs.sriwilis.data.mapping.MappingKatalog
import com.bs.sriwilis.data.mapping.MappingKategori
import com.bs.sriwilis.data.mapping.MappingNasabah
import com.bs.sriwilis.data.mapping.MappingPenarikan
import com.bs.sriwilis.data.mapping.MappingSampah
import com.bs.sriwilis.data.mapping.MappingTransaksi
import com.bs.sriwilis.data.network.ApiServiceMain
import com.bs.sriwilis.data.repository.modelhelper.CardCatalog
import com.bs.sriwilis.data.repository.modelhelper.CardCategory
import com.bs.sriwilis.data.repository.modelhelper.CardNasabah
import com.bs.sriwilis.data.repository.modelhelper.CardPenarikan
import com.bs.sriwilis.data.response.AdminData
import com.bs.sriwilis.data.response.AdminResponse
import com.bs.sriwilis.data.response.CartTransactionRequest
import com.bs.sriwilis.data.response.CatalogData
import com.bs.sriwilis.data.response.CatalogResponse
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.DataKeranjangItemResponse
import com.bs.sriwilis.data.response.GetAdminByIdResponse
import com.bs.sriwilis.data.response.GetUserByIdResponse
import com.bs.sriwilis.data.response.LoginResponseDTO
import com.bs.sriwilis.data.response.NasabahResponseDTO
import com.bs.sriwilis.data.response.OrderCartResponse
import com.bs.sriwilis.data.response.PenarikanListResponse
import com.bs.sriwilis.data.response.PenarikanResponse
import com.bs.sriwilis.data.response.PenarikanResponseDTO
import com.bs.sriwilis.data.response.PesananSampahItem
import com.bs.sriwilis.data.response.PesananSampahKeranjangResponse
import com.bs.sriwilis.data.response.PesanananSampahItemResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import com.bs.sriwilis.data.response.SingleAdminResponse
import com.bs.sriwilis.data.response.SingleCategoryResponse
import com.bs.sriwilis.data.response.SinglePesananSampahResponse
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransactionResponse
import com.bs.sriwilis.data.response.TransaksiSampahItem
import com.bs.sriwilis.data.response.TransaksiSampahItemResponse
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.data.room.dao.NasabahDao
import com.bs.sriwilis.data.room.entity.CatalogEntity
import com.bs.sriwilis.data.room.entity.CategoryEntity
import com.bs.sriwilis.data.room.entity.LoginResponseEntity
import com.bs.sriwilis.data.room.entity.NasabahEntity
import com.bs.sriwilis.data.room.entity.PenarikanEntity
import com.bs.sriwilis.data.room.entity.PesananSampahKeranjangEntity
import kotlinx.coroutines.flow.Flow
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.model.CartTransaction
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardStatus
import com.bs.sriwilispetugas.data.repository.modelhelper.CardTransaksi
import com.bs.sriwilispetugas.data.room.KeranjangTransaksiEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(
    private val apiService: ApiServiceMain,
    private val appDatabase: AppDatabase
) {

    private val mappingNasabah = MappingNasabah()
    private val mappingKategori = MappingKategori()
    private val mappingKatalog = MappingKatalog()
    private val mappingSampah = MappingSampah()
    private val mappingTransaksi = MappingTransaksi()
    private val mappingPenarikan = MappingPenarikan()


    suspend fun getToken(): String? {
        val loginResponse = appDatabase.loginResponseDao().getLoginResponseById(1)
        return loginResponse?.access_token ?: run {
            Log.e("MainRepository", "LoginResponseEntity is null or access token is missing")
            null
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            appDatabase.loginResponseDao().deleteAll()
        }
    }

    //CRUD Admin
    suspend fun getAdminData(): Result<AdminData> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getAdminData("Bearer $token")

            if (response.isSuccessful) {
                val adminDetailsResponse = response.body()
                if (adminDetailsResponse != null) {
                    val adminItem = adminDetailsResponse.data
                    if (adminItem != null) {
                        Result.Success(adminItem)
                    } else {
                        Result.Error("Admin not found")
                    }
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to fetch user details: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Log.e("GetUserDetails", "Error fetching user details", e)
            Result.Error("Error fetching user details: ${e.message}")
        }
    }

    suspend fun editAdmin(adminId: String, name: String, phone: String, address: String, image: String): Result<SingleAdminResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.editAdmin(adminId, "Bearer $token", name, phone, address, image)
            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("EditAdmin", "Edit error", e)
            Result.Error("Edit error: ${e.message}")
        }
    }

    suspend fun changePasswordAdmin(adminId: String, password: String): Result<AdminResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.changePasswordAdmin(adminId, token, password)
            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("EditAdmin", "Edit error", e)
            Result.Error("Edit error: ${e.message}")
        }
    }

    //CRUD Users

    suspend fun registerUser(phone: String, password: String, name: String, address: String, balance: String): Result<RegisterUserResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.registerUser("Bearer $token", phone, password, name, address, balance)
            if (response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse != null) {
                    Result.Success(registerResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to register: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("Register", "Registration error", e)
            Result.Error("Register error: ${e.message}")
        }
    }

    suspend fun editUser(userId: String, name: String, phone: String, address: String, balance: Double): Result<GetUserByIdResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.editUser(phone, "Bearer $token", name, phone, address, balance.toString())
            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("EditUser", "Edit error", e)
            Result.Error("Edit error: ${e.message}")
        }
    }

    suspend fun deleteUser(userPhone: String): Result<Boolean> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.deleteUser(userPhone, "Bearer $token")

            if (response.isSuccessful) {

                withContext(Dispatchers.IO) {
                    userPhone.let {
                        appDatabase.nasabahDao().deleteUserByPhone(userPhone)
                    }
                }

                Result.Success(true)
            } else {
                Result.Error("Failed to remove nasabah: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MainRepository", "Exception occurred: ${e.message}")
            Result.Error("Error occurred: ${e.message}")
        }
    }

    // Sinkronisasi Data

    suspend fun syncData(): Result<Unit>{
        return try {
            appDatabase.nasabahDao().deleteAll()
            appDatabase.categoryDao().deleteAll()
            appDatabase.catalogDao().deleteAll()
            appDatabase.pesananSampahKeranjangDao().deleteAll()
            appDatabase.pesananSampahDao().deleteAll()
            appDatabase.penarikanDao().deleteAllPenarikan()

            val nasabahResult = getAllNasabah()
            if (nasabahResult is Result.Error) {
                return Result.Error("Failed to sync nasabah: ${nasabahResult.error}")
            }

            val categoryResult = getCategory()
            if (categoryResult is Result.Error) {
                return Result.Error("Failed to sync category: ${categoryResult.error}")
            }

            val catalogResult = getAllCatalog()
            if (catalogResult is Result.Error) {
                return Result.Error("Failed to sync catalog: ${catalogResult.error}")
            }

            val pesananSampahKeranjangResult = getAllOrderWaste()
            if (pesananSampahKeranjangResult is Result.Error) {
                return Result.Error("Failed to sync pesanan sampah keranjang ${pesananSampahKeranjangResult.error}")
            }

            val transaksiDataResult = getAllTransaksi()
            if (transaksiDataResult is Result.Error) {
                return Result.Error("Failed to sync transaksi data sampah keranjang by id  ${transaksiDataResult.error}")
            }

            val penarikanResult = getPenarikan()
            if (penarikanResult is Result.Error) {
                return Result.Error("Failed to sync transaksi data sampah keranjang by id  ${penarikanResult.error}")
            }


            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error occured during synchronization: ${e.message}")
        }
    }

    // Fungsi sinkronisasi Nasabah
    suspend fun syncNasabah(): Result<Unit> {
        return try {
            val nasabahResult = getAllNasabah()
            if (nasabahResult is Result.Error) {
                return Result.Error("Failed to sync nasabah: ${nasabahResult.error}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error syncing nasabah: ${e.message}")
        }
    }

    // Fungsi sinkronisasi Kategori
    suspend fun syncCategory(): Result<Unit> {
        return try {
            val categoryResult = getCategory()
            if (categoryResult is Result.Error) {
                return Result.Error("Failed to sync category: ${categoryResult.error}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error syncing category: ${e.message}")
        }
    }

    // Fungsi sinkronisasi Katalog
    suspend fun syncCatalog(): Result<Unit> {
        return try {
            val catalogResult = getAllCatalog()
            if (catalogResult is Result.Error) {
                return Result.Error("Failed to sync catalog: ${catalogResult.error}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error syncing catalog: ${e.message}")
        }
    }

    // Fungsi sinkronisasi Pesanan Sampah Keranjang
    suspend fun syncPesananSampahKeranjang(): Result<Unit> {
        return try {
            val pesananSampahKeranjangResult = getAllOrderWaste()
            if (pesananSampahKeranjangResult is Result.Error) {
                return Result.Error("Failed to sync pesanan sampah keranjang: ${pesananSampahKeranjangResult.error}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error syncing pesanan sampah keranjang: ${e.message}")
        }
    }

    // Fungsi sinkronisasi Transaksi
    suspend fun syncTransaksi(): Result<Unit> {
        return try {
            val transaksiDataResult = getAllTransaksi()
            if (transaksiDataResult is Result.Error) {
                return Result.Error("Failed to sync transaksi data: ${transaksiDataResult.error}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error syncing transaksi data: ${e.message}")
        }
    }

    // Fungsi sinkronisasi Penarikan
    suspend fun syncPenarikan(): Result<Unit> {
        return try {
            val penarikanResult = getPenarikan()
            if (penarikanResult is Result.Error) {
                return Result.Error("Failed to sync penarikan data: ${penarikanResult.error}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error syncing penarikan data: ${e.message}")
        }
    }

    // Penutup Sinkronisasi Data


    suspend fun getAllNasabah(): Result<List<NasabahEntity>> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.getAllNasabah("Bearer $token")

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.Error("Response body is null")

                // Mapping dari DTO ke Entitas Room
                val nasabahEntities = mappingNasabah.mapNasabahResponseDtoToEntity(responseBody)

                // Simpan data ke database Room (opsional, jika perlu disimpan)
                withContext(Dispatchers.IO) {
                    appDatabase.nasabahDao().insert(nasabahEntities)
                }

                Result.Success(nasabahEntities)
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    /*suspend fun getUser(): Result<GetAllUserResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            Log.d("tokenmainrepository", "$token")
            val response = apiService.getAllUser("Bearer $token")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Response body is null")
                }
            } else {
                Result.Error("Failed to fetch saved news: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }*/

    //CRUD Categories
    suspend fun addCategory(
        name: String,
        price: String,
        type: String,
        imageBase64: String
    ): Result<CategoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val token = getToken() ?: ""
                val response = apiService.addCategory("Bearer $token", name, price, type, imageBase64)
                if (response.isSuccessful) {
                    val categoryResponse = response.body()
                    if (categoryResponse != null) {
                        Result.Success(categoryResponse)
                    } else {
                        Result.Error("Empty response body")
                    }
                } else {
                    Result.Error("Failed to fetch saved news: ${response.message()} (${response.code()})")
                }
            } catch (e: Exception) {
                Result.Error("Exception occured: ${e.message}")
            }
        }
    }

    suspend fun getCategory(): Result<List<CategoryEntity>> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            Log.d("tokenmainrepository", "$token")
            val response = apiService.getAllCategory("Bearer $token")

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.Error("Response body is null")

                // Mapping dari DTO ke Entitas Room
                val categoryEntities = mappingKategori.mapCategoryResponseDtoToEntity(responseBody)

                // Simpan data ke database Room (opsional, jika perlu disimpan)
                withContext(Dispatchers.IO) {
                    appDatabase.categoryDao().insert(categoryEntities)
                }

                Result.Success(categoryEntities)
            } else {
                Result.Error("Failed to fetch saved news: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun editCategory(categoryId: String, name: String, price: String, type: String, image: String): Result<SingleCategoryResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.editCategory(categoryId, "Bearer $token", name, price, type, image)
            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Edit error: ${e.message}")
        }
    }

    suspend fun deleteCategory(categoryId: String): Result<Boolean> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.deleteCategory(categoryId, "Bearer $token")

            if (response.isSuccessful) {

                withContext(Dispatchers.IO) {
                    categoryId.let {
                        appDatabase.categoryDao().deleteCategoryById(categoryId)
                    }
                }

                Result.Success(true)
            } else {
                Result.Error("Failed to remove kategori: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MainRepository", "Exception occurred: ${e.message}")
            Result.Error("Error occurred: ${e.message}")
        }
    }

    //CATALOG CRUD
    suspend fun addCatalog(
        catalogId: String,
        name: String,
        desc: String,
        price: String,
        number: String,
        link: String,
        image: String,
    ): Result<CatalogResponse> {
        val token = getToken() ?: return Result.Error("Token is null")

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.addCatalog("Bearer $token", name, desc, price, number, link, image)
                if (response.isSuccessful) {
                    val categoryResponse = response.body()
                    if (categoryResponse != null) {
                        Result.Success(categoryResponse)
                    } else {
                        Result.Error("Empty response body")
                    }
                } else {
                    Result.Error("Failed to fetch saved news: ${response.message()} (${response.code()})")
                }
            } catch (e: Exception) {
                Result.Error("Exception occured: ${e.message}")
            }
        }
    }

    suspend fun getAllCatalog(): Result<List<CatalogEntity>> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.getAllCatalog("Bearer $token")

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.Error("Response body is null")

                val catalogEntities = mappingKatalog.mapCatalogResponseDtoToEntity(responseBody)

                withContext(Dispatchers.IO) {
                    Log.d("Database", "Inserting data into the catalog table")
                    appDatabase.catalogDao().insert(catalogEntities)
                }

                Result.Success(catalogEntities)
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun getCatalogById(id: String): Result<CatalogData> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getCatalogById(id, "Bearer $token")

            if (response.isSuccessful) {
                val categoryDetailResponse = response.body()
                if (categoryDetailResponse != null) {
                    val categoryItem = categoryDetailResponse.data
                    if (categoryItem != null) {
                        Result.Success(categoryItem)
                    } else {
                        Result.Error("Catalog not found")
                    }
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to fetch catalog details: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Log.e("GetCatalogDetails", "Error fetching catalog details", e)
            Result.Error("Error fetching user details: ${e.message}")
        }
    }

    suspend fun editCatalog(
        categoryId: String,
        name: String,
        desc: String,
        price: String,
        number: String,
        link: String,
        image: String,
    ): Result<CatalogResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.editCatalog(categoryId, "Bearer $token", name, desc, price, number, link, image)

            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("EditCategory", "Edit error", e)
            Result.Error("Edit error: ${e.message}")
        }
    }

    suspend fun deleteCatalog(categoryId: String): Result<Boolean> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.deleteCatalog(categoryId, "Bearer $token")

            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error("Failed to remove bookmark: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MainRepository", "Exception occurred: ${e.message}")
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun getAllOrderWaste(): Result<List<PesananSampahKeranjangEntity>> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.getAllPesananSampah("Bearer $token")

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.Error("Response body is null")

                // Mapping dari DTO ke Entitas Room
                val (orderEntities, wasteEntities) = mappingSampah.mapPesananSampahApiResponseDtoToEntities(responseBody)

                // Simpan data ke database Room (opsional, jika perlu disimpan)
                withContext(Dispatchers.IO) {
                    appDatabase.pesananSampahKeranjangDao().insert(orderEntities)
                    appDatabase.pesananSampahDao().insert(wasteEntities)
                }

                Result.Success(orderEntities)
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun getOrderCartByNasabahId(idNasabah: String): Result<OrderCartResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getOrderCartByNasabahId("Bearer $token", idNasabah)

            Log.d("Request", "Token: Bearer $token, idNasabah: $idNasabah")
            Log.d("Response", response.body()?.toString() ?: "Response body is null")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Response body is null")
                }
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun getAllTransaksi(): Result<List<KeranjangTransaksiEntity>> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getAllTransaction("Bearer $token")
            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.Error("Response body is null")

                // Mapping dari DTO ke Entitas Room
                val (keranjangEntities, sampahEntities) = mappingTransaksi.mapTransaksiSampahApiResponseDtoToEntities(responseBody)

                // Simpan data ke database Room (opsional, jika perlu disimpan)
                withContext(Dispatchers.IO) {
                    appDatabase.keranjangTransaksiDao().insertAllKeranjangTransaksi(keranjangEntities)
                    appDatabase.transaksiSampahDao().insertAllTransaksiSampah(sampahEntities)
                }
                Result.Success(keranjangEntities)
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun getPenarikan(): Result<List<PenarikanEntity>> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getAllMutation("Bearer $token")

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.Error("Response body is null")
                Log.d("GetPenarikan", "Response body: $responseBody")

                // Mapping dari DTO ke Entitas Room
                val penarikanEntities = mappingPenarikan.mapPenarikanResponseDtoToEntity(responseBody)
                Log.d("MappedEntities", penarikanEntities.toString())

                // Simpan data ke database Room (opsional, jika perlu disimpan)
                withContext(Dispatchers.IO) {
                    try {
                        appDatabase.penarikanDao().insert(penarikanEntities)
                        Log.d("PenarikanInsertion", "Insertion successful")
                    } catch (e: Exception) {
                        Log.e("PenarikanInsertion", "Error inserting into DB: ${e.message}")
                    }
                }

                Result.Success(penarikanEntities)
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun getTransactionDataItem(idNasabah: String): Result<PesanananSampahItemResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getTransactionDetailByNasabahId("Bearer $token", idNasabah)

            Log.d("Request", "Token: Bearer $token, idNasabah: $idNasabah")
            Log.d("Response", response.body()?.toString() ?: "Response body is null")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Response body is null")
                }
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun getDataKeranjangItem(idNasabah: String): Result<DataKeranjangItemResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getDataKeranjangDetailByNasabahId("Bearer $token", idNasabah)

            Log.d("Request", "Token: Bearer $token, idNasabah: $idNasabah")
            Log.d("Response", response.body()?.toString() ?: "Response body is null")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Response body is null")
                }
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun registerDate(orderId: String, date: String): Result<SinglePesananSampahResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.registerDate(orderId, "Bearer $token", date)
            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("EditUser", "Edit error", e)
            Result.Error("Edit error: ${e.message}")
        }
    }

    suspend fun updateSudahDijadwalkan(orderId: String): Result<SinglePesananSampahResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.updateStatusSudahDijadwalkan(orderId, "Bearer $token")
            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("EditUser", "Edit error", e)
            Result.Error("Edit error: ${e.message}")
        }
    }

    suspend fun updateOrderFailed(orderId: String): Result<SinglePesananSampahResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.updateStatusGagal(orderId, token)
            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("EditUser", "Edit error", e)
            Result.Error("Edit error: ${e.message}")
        }
    }

    // Get Local Data from Dao

    suspend fun getAllNasabahDao(): Result<List<CardNasabah>> {
        return withContext(Dispatchers.IO) {
            try {
                val nasabahData = appDatabase.nasabahDao().getAllNasabah()
                Result.Success(nasabahData)
            } catch (e: Exception) {
                Result.Error("Error occured: ${e.message}")
            }
        }
    }

    suspend fun getNasabahPhone(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val nasabahData = appDatabase.nasabahDao().getNasabahByPhone()
                Result.Success(nasabahData)
            } catch (e: Exception) {
                Result.Error("Error occured: ${e.message}")
            }
        }
    }

    suspend fun getNasabahById(userId: String): Result<CardNasabah> {
        return withContext(Dispatchers.IO) {
            try {
                val nasabahData = appDatabase.nasabahDao().getNasabahById(userId)
                Result.Success(nasabahData)
            } catch (e: Exception) {
                Result.Error("Error occured: ${e.message}")
            }
        }
    }

    suspend fun getNasabahByPhone(phone: String): Result<CardNasabah> {
        return withContext(Dispatchers.IO) {
            try {
                val nasabahData = appDatabase.nasabahDao().getNasabahByPhone(phone)
                Result.Success(nasabahData)
            } catch (e: Exception) {
                Result.Error("Error occured: ${e.message}")
            }
        }
    }

    suspend fun getAllCategoriesDao(): Result<List<CardCategory>> {
        return withContext(Dispatchers.IO) {
            try {
                val categoryData = appDatabase.categoryDao().getAllCategory()
                Result.Success(categoryData)
            } catch (e: Exception) {
                Result.Error("Error occured: ${e.message}")
            }
        }
    }

    suspend fun getCategoryByIdDao(categoryId: String): Result<CardCategory> {
        return withContext(Dispatchers.IO) {
            try {
                val nasabahData = appDatabase.categoryDao().getCategoryById(categoryId)
                Result.Success(nasabahData)
            } catch (e: Exception) {
                Result.Error("Error occured: ${e.message}")
            }
        }
    }

    suspend fun getCatalogByIdDao(catalogId: String): Result<CardCatalog> {
        return withContext(Dispatchers.IO) {
            try {
                val nasabahData = appDatabase.catalogDao().getCatalogById(catalogId)
                Result.Success(nasabahData)
            } catch (e: Exception) {
                Result.Error("Error occured: ${e.message}")
            }
        }
    }

    suspend fun getAllCatalogDao(): Result<List<CardCatalog>> {
        return withContext(Dispatchers.IO) {
            try {
                val catalogData = appDatabase.catalogDao().getAllCatalog()
                Result.Success(catalogData)
            } catch (e: Exception) {
                Result.Error("Error occured: ${e.message}")
            }
        }
    }

    suspend fun getPerkiraanById(orderId: String): Result<String?> {
        return withContext(Dispatchers.IO) {
            try {
                val beratPerkiraan = appDatabase.pesananSampahDao().getBeratPerkiraanById(orderId)
                Result.Success(beratPerkiraan)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }

    suspend fun getPesananSampahKeranjangDetailList(idPesanan: String): Result<List<CardDetailPesanan>> {
        return withContext(Dispatchers.IO) {
            try {
                val combinedData = appDatabase.pesananSampahKeranjangDao().getPesananSampahKeranjangDetailList(idPesanan)
                Result.Success(combinedData)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }

    suspend fun getPesananSampahKeranjang(): Result<List<CardPesanan>> {
        return withContext(Dispatchers.IO) {
            try {
                val pesananSampahKeranjang = appDatabase.pesananSampahKeranjangDao().getPesananSampahKeranjang()
                Result.Success(pesananSampahKeranjang)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }

    suspend fun getPesananSampahKeranjangDetail(idPesanan: String): Result<CardPesanan> {
        return withContext(Dispatchers.IO) {
            try {
                val detailPesananSampahKeranjang = appDatabase.pesananSampahKeranjangDao().getDataDetailPesananSampahKeranjang(idPesanan)
                Result.Success(detailPesananSampahKeranjang)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }

    suspend fun getCombinedTransaksiData(): Result<List<CardTransaksi>> {
        return withContext(Dispatchers.IO) {
            try {
                val detailPesananSampahKeranjang = appDatabase.keranjangTransaksiDao().getCombinedTransaksiData()
                Result.Success(detailPesananSampahKeranjang)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }

    suspend fun getStatusOrderHistory(): Result<CardStatus> {
        return withContext(Dispatchers.IO) {
            try {
                val detailPesananSampahKeranjang = appDatabase.keranjangTransaksiDao().getStatusKeranjangTransaksi()
                Result.Success(detailPesananSampahKeranjang)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }

    suspend fun getPenarikanDao(): Result<List<CardPenarikan>> {
        return withContext(Dispatchers.IO) {
            try {
                val detailPesananSampahKeranjang = appDatabase.penarikanDao().getAllPenarikan()
                Result.Success(detailPesananSampahKeranjang)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }

    // End Get locale from dao

    // Transaction CRUD
    suspend fun addCartTransaction(
        idNasabah: String,
        tanggal: String,
        transaksi_sampah: List<CartTransaction> // Keep using CartTransaction
    ): Result<TransaksiSampahItemResponse?> {

        val token = getToken() ?: return Result.Error("Token is null")
        return try {
            // Prepare the transaction items for the request
            val transaksiSampahItems = transaksi_sampah.map { cartTransaction ->
                TransaksiSampahItem(
                    gambar = cartTransaction.gambar ?: null,
                    berat = cartTransaction.berat,
                    kategori = cartTransaction.kategori,
                )
            }

            // Create the request body
            val cartTransactionRequest = CartTransactionRequest(
                idNasabah = idNasabah,
                tanggal = tanggal,
                transaksiSampah = transaksiSampahItems
            )

            // Make the API request
            val response = apiService.addCartTransaction("Bearer $token", cartTransactionRequest)
            Log.i("infokan", cartTransactionRequest.toString())

            if (response.isSuccessful) {
                Result.Success(response.body())
            } else {
                // Log the status code and the error body (if available)
                val statusCode = response.code()
                val errorBody = response.errorBody()?.string()

                Log.e("AddCartTransaction", "API call failed with status code: $statusCode")
                Log.e("AddCartTransaction", "Error body: $errorBody")

                Result.Error("Failed to add cart transaction: Status code $statusCode, Error body: $errorBody")
            }

        } catch (e: Exception) {
            // Log the exception with the full stack trace
            Log.e("AddCartTransactionError", "Error adding cart transaction", e)

            // Return the error message and the exception as a string
            Result.Error(e.toString())
        }
    }

    suspend fun updateStatus(mutationId: String, statusPenarikan: String, nomorToken: String? = null): Result<PenarikanResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = if (nomorToken != null) {
                apiService.updateMutationStatus(mutationId, "Bearer $token", status_penarikan = statusPenarikan, nomor_token = nomorToken)
            } else {
                apiService.updateMutationStatusWithoutToken(mutationId, "Bearer $token", statusPenarikan)
            }
            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit status penarikan: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("EditUser", "Edit error", e)
            Result.Error("Edit error: ${e.message}")
        }
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(apiServiceMain: ApiServiceMain, appDatabase: AppDatabase): MainRepository {
            return instance ?: synchronized(this) {
                instance ?: MainRepository(apiServiceMain, appDatabase).also { instance = it }
            }
        }
    }
}