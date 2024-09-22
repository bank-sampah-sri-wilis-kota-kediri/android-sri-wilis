package com.bs.sriwilis.helper

import android.content.Context
import com.bs.sriwilis.data.AppDatabase
import com.bs.sriwilis.data.repository.AuthRepository
import com.bs.sriwilis.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object InjectionAuth {
    fun provideRepository(context: Context): AuthRepository {
        val apiServiceAuth = ApiConfig.getAuthService()
        val appDatabase = AppDatabase.getInstance(context)
        return AuthRepository.getInstance(apiServiceAuth, appDatabase)
    }
}

object InjectionMain {
    fun provideRepository(context: Context): MainRepository {
        val appDatabase = AppDatabase.getInstance(context)
        val mainRepository = MainRepository.getInstance(ApiConfig.getMainService(""), appDatabase)
        val token: String = runBlocking(Dispatchers.IO) {
            mainRepository.getToken() ?: ""
        }

        val apiServiceMain = ApiConfig.getMainService(token ?: "")
        return MainRepository.getInstance(apiServiceMain, appDatabase)
    }
}