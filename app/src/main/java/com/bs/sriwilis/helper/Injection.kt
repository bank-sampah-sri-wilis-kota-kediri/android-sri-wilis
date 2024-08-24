package com.bs.sriwilis.helper

import android.content.Context
import com.bs.sriwilis.data.preference.UserPreferences
import com.bs.sriwilis.data.preference.dataStore
import com.bs.sriwilis.data.repository.AuthRepository
import com.bs.sriwilis.data.repository.MainRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object InjectionAuth {
    fun provideRepository(context: Context): AuthRepository {
        val dataStore = context.dataStore
        val userPreferences = UserPreferences.getInstance(dataStore)
        val apiServiceAuth = ApiConfig.getAuthService()
        return AuthRepository.getInstance(apiServiceAuth, userPreferences)
    }
}

object InjectionMain {
    fun provideRepository(context: Context): MainRepository {
        val dataStore = context.dataStore
        val userPreferences = UserPreferences.getInstance(dataStore)
        val token = runBlocking { userPreferences.token.first() }
        val apiServiceMain = ApiConfig.getMainService(token ?: "")
        return MainRepository.getInstance(apiServiceMain, userPreferences)
    }
}