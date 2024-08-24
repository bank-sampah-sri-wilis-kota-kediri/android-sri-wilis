package com.bs.sriwilis.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bs.sriwilis.data.repository.MainRepository

class WelcomeViewModel(private val repository: MainRepository) : ViewModel() {

    fun getToken(): LiveData<String?> {
        return repository.getToken().asLiveData()
    }

}
