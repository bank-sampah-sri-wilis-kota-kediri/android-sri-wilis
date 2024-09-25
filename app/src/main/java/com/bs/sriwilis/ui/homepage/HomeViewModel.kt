package com.bs.sriwilis.ui.homepage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.AdminData
import com.bs.sriwilis.data.response.AdminResponse
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _changeResult = MutableLiveData<Result<AdminResponse>>()
    val changeResult: LiveData<Result<AdminResponse>> = _changeResult

    private val _adminData = MutableLiveData<Result<AdminData?>>()
    val adminData: LiveData<Result<AdminData?>> get() = _adminData

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
}