package com.bs.sriwilis.data.repository

import android.util.Log
import com.bangkit.factha.data.network.ApiServiceAuth
import com.bs.sriwilis.data.preference.UserPreferences
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.data.response.LoginResponse

class AuthRepository(
    private val apiService: ApiServiceAuth,
    private val userPreferences: UserPreferences
) {
    suspend fun login(phone: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(phone, password)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse?.data != null) {
                    loginResponse.data.let { data ->
                        data.accessToken?.let { it ->
                            userPreferences.saveUser(it)
                        }
                        Result.Success(loginResponse)
                    }
                } else {
                    Result.Error("Empty Response Body")
                }
            } else {
                Result.Error("Failed to login: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(apiService: ApiServiceAuth, userPreferences: UserPreferences): AuthRepository {
            return instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPreferences).also { instance = it }
            }
        }
    }
}
