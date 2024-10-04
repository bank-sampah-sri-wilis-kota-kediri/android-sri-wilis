package com.bs.sriwilis.ui.homepage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.repository.modelhelper.CardNasabah
import com.bs.sriwilis.data.response.AdminData
import com.bs.sriwilis.data.response.AdminResponse
import com.bs.sriwilis.data.response.UserData
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _changeResult = MutableLiveData<Result<AdminResponse>>()
    val changeResult: LiveData<Result<AdminResponse>> = _changeResult

    private val _adminData = MutableLiveData<Result<AdminData?>>()
    val adminData: LiveData<Result<AdminData?>> get() = _adminData

    private val _userData = MutableLiveData<Result<List<CardNasabah?>>>()
    val userData: LiveData<Result<List<CardNasabah?>>> get() = _userData

    fun fetchAdminDetails() {
        viewModelScope.launch {
            _adminData.value = Result.Loading
            when (val result = mainRepository.getAdminData()) {
                is Result.Success -> {
                    _adminData.value = Result.Success(result.data)
                }
                is Result.Error -> {
                    _adminData.value = Result.Error(result.error)
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }

                Result.Loading -> TODO()
            }
        }
    }

    fun fetchUserSaldo() {
        viewModelScope.launch {
            _userData.value = Result.Loading
            when (val result = mainRepository.getAllNasabahDao()) {
                is Result.Success -> {
                    _userData.value = Result.Success(result.data)
                }
                is Result.Error -> {
                    _userData.value = Result.Error(result.error)
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }

                Result.Loading -> TODO()
            }
        }
    }

    suspend fun syncData(): Result<Unit> {
        return mainRepository.syncData()
    }
}