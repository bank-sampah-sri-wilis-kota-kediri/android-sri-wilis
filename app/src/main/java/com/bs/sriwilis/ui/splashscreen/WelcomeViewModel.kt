package com.bs.sriwilis.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.data.repository.MainRepository

class WelcomeViewModel(private val repository: MainRepository) : ViewModel() {

    suspend fun getToken(): String? {
        return repository.getToken()
    }

    suspend fun syncData(): Result<Unit> {
        return repository.syncData()
    }

}
