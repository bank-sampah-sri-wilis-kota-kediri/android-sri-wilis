package com.bs.sriwilis.ui.homepage.operation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.repository.modelhelper.CardNasabah
import com.bs.sriwilis.data.response.GetUserByIdResponse
import com.bs.sriwilis.data.response.NasabahResponseDTO
import com.bs.sriwilis.data.response.RegisterUserResponse
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.data.room.entity.NasabahEntity
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result

class ManageUserViewModel(private val repository: MainRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<Result<RegisterUserResponse>>()
    val registerResult: LiveData<Result<RegisterUserResponse>> = _registerResult

    private val _nasabah = MutableLiveData<Result<List<CardNasabah>>>()
    val nasabah: LiveData<Result<List<CardNasabah>>> get() = _nasabah

    private val _users = MutableLiveData<Result<GetUserByIdResponse>>()
    val users: LiveData<Result<GetUserByIdResponse>> get() = _users

    private val _usersData = MutableLiveData<Result<CardNasabah>>()
    val usersData: LiveData<Result<CardNasabah>> get() = _usersData

    private val _deleteResult = MutableLiveData<Result<Boolean>>()
    val deleteResult: LiveData<Result<Boolean>> get() = _deleteResult

    private val _searchResults = MutableLiveData<List<CardNasabah>>()
    val searchResults: LiveData<List<CardNasabah>> get() = _searchResults

    fun deleteUser(userPhone: String) {
        viewModelScope.launch {
            _deleteResult.postValue(Result.Loading)
            try {
                when (val result = repository.deleteUser(userPhone)) {
                    is Result.Success -> {
                        if (result.data) {
                            _deleteResult.postValue(Result.Success(true))
                        } else {
                            _deleteResult.postValue(Result.Error("Failed to delete user"))
                        }
                    }
                    is Result.Error -> {
                        _deleteResult.postValue(Result.Error(result.error ?: "Unknown error occurred"))
                    }
                    Result.Loading -> {
                    }
                }
            } catch (e: Exception) {
                _deleteResult.postValue(Result.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }

    fun register(phone: String, password: String, name: String, address: String, balance: String) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            val result = repository.registerUser(phone, password, name, address, balance)
            _registerResult.value = result
        }
    }

    fun editUser(phone: String, name: String, address: String, balance: Double) {
        viewModelScope.launch {
            _users.value = Result.Loading
            val result = repository.editUser(phone, name, address, balance)
            _users.value = result
        }
    }

    suspend fun getUsers() {
        viewModelScope.launch {
            _nasabah.postValue(Result.Loading)
            val result = repository.getAllNasabahDao()
            _nasabah.postValue(result)
        }
    }

    fun searchUsers(name: String) {
        viewModelScope.launch {
            val result = repository.searchNasabahDao(name)
            if (result is Result.Success) {
                _searchResults.value = result.data
            } else {
                _searchResults.value = emptyList()
            }
        }
    }

    fun fetchUserDetails(phone: String) {
        viewModelScope.launch {
            _usersData.value = Result.Loading
            when (val result = repository.getNasabahByPhone(phone)) {
                is Result.Success -> {
                    _usersData.value = Result.Success(result.data)
                }
                is Result.Error -> {
                    _usersData.value = Result.Error(result.error)
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }

                Result.Loading -> TODO()
            }
        }
    }

    suspend fun syncData(): Result<Unit> {
        return repository.syncNasabah()
    }

}