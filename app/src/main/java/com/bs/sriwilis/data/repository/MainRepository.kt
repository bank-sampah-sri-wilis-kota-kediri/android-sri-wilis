package com.bs.sriwilis.data.repository

import android.provider.ContactsContract.Data
import android.util.Log
import com.bs.sriwilis.data.AppDatabase
import com.bs.sriwilis.data.mapping.MappingKategori
import com.bs.sriwilis.data.mapping.MappingNasabah
import com.bs.sriwilis.data.network.ApiServiceMain
import com.bs.sriwilis.data.repository.modelhelper.CardCategory
import com.bs.sriwilis.data.repository.modelhelper.CardNasabah
import com.bs.sriwilis.data.response.AdminData
import com.bs.sriwilis.data.response.AdminResponse
import com.bs.sriwilis.data.response.CartTransactionRequest
import com.bs.sriwilis.data.response.CatalogData
import com.bs.sriwilis.data.response.CatalogResponse
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.DataKeranjangItem
import com.bs.sriwilis.data.response.DataKeranjangItemResponse
import com.bs.sriwilis.data.response.GetAdminByIdResponse
import com.bs.sriwilis.data.response.GetUserByIdResponse
import com.bs.sriwilis.data.response.LoginResponseDTO
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
import com.bs.sriwilis.data.response.TransaksiSampahItemResponse
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.data.room.dao.NasabahDao
import com.bs.sriwilis.data.room.entity.CategoryEntity
import com.bs.sriwilis.data.room.entity.LoginResponseEntity
import com.bs.sriwilis.data.room.entity.NasabahEntity
import kotlinx.coroutines.flow.Flow
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.model.CartTransaction
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

            val response = apiService.editAdmin(adminId, "Bearer $token", phone, name, address, image)
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

    suspend fun getUserById(userId: String): Result<UserItem> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getUserById(userId, "Bearer $token")

            if (response.isSuccessful) {
                val userDetailsResponse = response.body()
                if (userDetailsResponse != null) {
                    val userItem = userDetailsResponse.data
                    if (userItem != null) {
                        Result.Success(userItem)
                    } else {
                        Result.Error("User not found")
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

    suspend fun editUser(userId: String, phone: String, name: String, address: String, balance: Double): Result<GetUserByIdResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.editUser(userId, "Bearer $token", phone, name, address, balance.toString())
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


    // untuk mengambil data

    suspend fun syncData(): Result<Unit>{
        return try {
            val nasabahResult = getAllNasabah()
            if (nasabahResult is Result.Error) {
                return Result.Error("Failed to sync nasabah: ${nasabahResult.error}")
            }

            val categoryResult = getCategory()
            if (categoryResult is Result.Error) {
                return Result.Error("Failed to sync nasabah: ${categoryResult.error}")
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error occured during synchronization: ${e.message}")
        }

    }

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
                val response = apiService.addCategory(token, name, price, type, imageBase64)
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

    suspend fun getCategoryById(id: String): Result<CategoryData> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getCategoryById(id, "Bearer $token")

            if (response.isSuccessful) {
                val categoryDetailResponse = response.body()
                if (categoryDetailResponse != null) {
                    val categoryItem = categoryDetailResponse.data
                    if (categoryItem != null) {
                        Result.Success(categoryItem)
                    } else {
                        Result.Error("User not found")
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

    suspend fun editCategory(categoryId: String, name: String, price: String, type: String, image: String): Result<SingleCategoryResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.editCategory(categoryId, token, name, price, type, image)
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
    ): Result<SingleCatalogResponse> {
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

    suspend fun getCatalog(): Result<CatalogResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            Log.d("tokenmainrepository", "$token")
            val response = apiService.getAllCatalog("Bearer $token")

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
    ): Result<SingleCatalogResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.editCatalog(categoryId, token, name, desc, price, number, link, image)

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

    suspend fun getAllOrderSchedule(): Result<PesananSampahKeranjangResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getAllOrderSchedule("Bearer $token")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val filteredOrders = body.dataKeranjang
                    body.dataKeranjang = filteredOrders
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

    suspend fun getNasabahPhonesDao(): Result<List<String>> {
        return try {
            val phones = appDatabase.nasabahDao().getNasabahPhones()
            Result.Success(phones)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error fetching phone numbers")
        }
    }

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
                    harga = cartTransaction.harga,
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



    suspend fun getAllTransaction(): Result<TransactionResponse> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            Log.d("tokenmainrepository", "$token")
            val response = apiService.getAllTransaction("Bearer $token")

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