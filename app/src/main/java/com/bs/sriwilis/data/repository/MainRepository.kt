package com.bs.sriwilis.data.repository

import android.util.Log
import com.bs.sriwilis.data.network.ApiServiceMain
import com.bs.sriwilis.data.preference.UserPreferences
import com.bs.sriwilis.data.response.AddCategoryRequest
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
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

    suspend fun registerUser(phone: String, password: String, name: String, address: String, balance: String): Result<RegisterUserResponse> {
        return try {
            val response = apiService.registerUser(phone, password, name, address, balance)
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

    suspend fun getUser(): Result<GetAllUserResponse> {
        return try {
            val token = userPreferences.token.first() ?: ""
            Log.d("tokenmainrepository", "$token")
            val response = apiService.getAllNews("Bearer $token")

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

    suspend fun addCategory(
        token: String,
        name: String,
        price: String,
        type: String,
        imageBase64: String
    ): Result<CategoryResponse> {
        val categoryRequest = AddCategoryRequest(
            nama_kategori = name,
            harga_kategori = price,
            jenis_kategori = type,
            gambar_kategori = imageBase64
        )
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.addCategory("Bearer $token", categoryRequest)
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