package com.bs.sriwilis.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.AdminData
import com.bs.sriwilis.data.response.AdminResponse
import com.bs.sriwilis.data.response.SingleAdminResponse
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.launch

class AdminViewModel(private val repository: MainRepository): ViewModel() {
    private val _changeResult = MutableLiveData<Result<SingleAdminResponse>>()
    val changeResult: LiveData<Result<SingleAdminResponse>> = _changeResult

    private val _changeResultPassword = MutableLiveData<Result<AdminResponse>>()
    val changeResultPassword: LiveData<Result<AdminResponse>> = _changeResultPassword

    private val _admin = MutableLiveData<Result<List<AdminData?>>>()
    val admin: LiveData<Result<List<AdminData?>>> get() = _admin

    private val _adminData = MutableLiveData<Result<AdminData?>>()
    val adminData: LiveData<Result<AdminData?>> get() = _adminData

    fun editAdmin(adminId: String, name: String, phone: String, address: String, image: String) {
        viewModelScope.launch {
            _changeResult.value = Result.Loading
            val result = repository.editAdmin(adminId, name, phone, address, image)
            _changeResult.value = result
        }
    }

    fun fetchAdminDetails() {
        viewModelScope.launch {
            _adminData.value = Result.Loading
            when (val result = repository.getAdminData()) {
                is Result.Success -> {
                    _adminData.postValue(Result.Success(result.data))
                }
                is Result.Error -> {
                    _adminData.postValue(Result.Error(result.error))
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }

                Result.Loading -> TODO()
            }
        }
    }

    fun changePassword(adminId: String, password: String) {
        viewModelScope.launch {
            _changeResultPassword.value = Result.Loading
            val result = repository.changePasswordAdmin(adminId, password)
            _changeResultPassword.value = result
        }
    }
    
}