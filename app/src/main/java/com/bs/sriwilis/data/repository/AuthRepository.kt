package com.bs.sriwilis.data.repository

import android.util.Log
import com.bs.sriwilis.data.AppDatabase
import com.bs.sriwilis.data.network.ApiServiceAuth
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.data.response.LoginResponse
import com.bs.sriwilis.data.response.LoginResponseDTO
import com.bs.sriwilis.data.room.entity.LoginResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiService: ApiServiceAuth,
    private val appDatabase: AppDatabase
) {
    suspend fun login(phone: String, password: String): Result<LoginResponseDTO> {
        return try {
            val response = apiService.login(phone, password)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    // Map DTO ke Entity
                    val loginResponseEntity = mapDtoToEntity(loginResponse)
                    // Simpan ke Room
                    saveLoginResponseToRoom(loginResponseEntity)

                    Result.Success(loginResponse)
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

    private fun mapDtoToEntity(dto: LoginResponseDTO): LoginResponseEntity {
        return LoginResponseEntity(
            id = 1,
            success = dto.success,
            message = dto.message,
            access_token = dto.data?.access_token,
            nama_admin = dto.data?.nama_admin ?: "",
            no_hp_admin = dto.data?.no_hp_admin ?: "",
            alamat_admin = dto.data?.alamat_admin ?: "",
            jenis_admin = dto.data?.jenis_admin ?: "",
            password_admin = dto.data?.password_admin ?: "",
            gambar_admin = dto.data?.gambar_admin ?: ""

        )
    }

    private suspend fun saveLoginResponseToRoom(loginResponse: LoginResponseEntity) {
        withContext(Dispatchers.IO) {
            Log.d("Saving login response", loginResponse.access_token.toString())
            appDatabase.loginResponseDao().insert(loginResponse)
        }
    }


    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(apiService: ApiServiceAuth, appDatabase: AppDatabase): AuthRepository {
            return instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, appDatabase).also { instance = it }
            }
        }
    }
}
