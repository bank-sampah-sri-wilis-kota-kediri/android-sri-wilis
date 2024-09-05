package com.bs.sriwilis.data.repository

import android.util.Log
import com.bs.sriwilis.data.network.ApiServiceMain
import com.bs.sriwilis.data.preference.UserPreferences
import com.bs.sriwilis.data.response.CatalogData
import com.bs.sriwilis.data.response.CatalogResponse
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import com.bs.sriwilis.data.response.UserItem
import kotlinx.coroutines.flow.Flow
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class MainRepository(
    private val apiService: ApiServiceMain,
    val userPreferences: UserPreferences
) {

    fun getToken(): Flow<String?> {
        return userPreferences.token
    }

    suspend fun logout() {
        userPreferences.clearUserDetails()
    }

    //CRUD Users

    suspend fun registerUser(phone: String, password: String, name: String, address: String, balance: String): Result<RegisterUserResponse> {
        return try {
            val token = userPreferences.token.first() ?: ""

            val response = apiService.registerUser(token, phone, password, name, address, balance)
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
            val token = userPreferences.token.first() ?: ""
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



    suspend fun editUser(userId: String, phone: String, name: String, address: String, balance: Double): Result<RegisterUserResponse> {
        return try {
            val token = userPreferences.token.first() ?: ""

            val response = apiService.editUser(userId, token, phone, name, address, balance)
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

    suspend fun deleteUser(userId: String): Result<Boolean> {
        return try {
            val token = userPreferences.token.first() ?: ""
            val response = apiService.deleteUser(userId, "Bearer $token")

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

    suspend fun getUser(): Result<GetAllUserResponse> {
        return try {
            val token = userPreferences.token.first() ?: ""
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
    }

    //CRUD Categories
    suspend fun addCategory(
        token: String,
        name: String,
        price: String,
        type: String,
        imageBase64: String
    ): Result<CategoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
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

    suspend fun getCategory(): Result<CategoryResponse> {
        return try {
            val token = userPreferences.token.first() ?: ""
            Log.d("tokenmainrepository", "$token")
            val response = apiService.getAllCategory("Bearer $token")

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

    suspend fun getCategoryById(id: String): Result<CategoryData> {
        return try {
            val token = userPreferences.token.first() ?: ""
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

    suspend fun editCategory(categoryId: String, name: String, price: String, type: String, image: String): Result<CategoryResponse> {
        return try {
            val token = userPreferences.token.first() ?: ""

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
            val token = userPreferences.token.first() ?: ""
            val response = apiService.deleteCategory(categoryId, "Bearer $token")

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
        val token = userPreferences.token.first() ?: ""

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
            val token = userPreferences.token.first() ?: ""
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
            val token = userPreferences.token.first() ?: ""
            val response = apiService.getCatalogById(id, "Bearer $token")

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
            val token = userPreferences.token.first() ?: ""

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
            val token = userPreferences.token.first() ?: ""
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

    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(apiServiceMain: ApiServiceMain, userPreferences: UserPreferences): MainRepository {
            return instance ?: synchronized(this) {
                instance ?: MainRepository(apiServiceMain, userPreferences).also { instance = it }
            }
        }
    }
}