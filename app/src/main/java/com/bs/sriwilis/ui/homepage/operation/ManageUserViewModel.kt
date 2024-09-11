package com.bs.sriwilis.ui.homepage.operation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import com.bs.sriwilis.data.response.SingleUserResponse
import com.bs.sriwilis.data.response.UserItem
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result

class ManageUserViewModel(private val repository: MainRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<Result<SingleUserResponse>>()
    val registerResult: LiveData<Result<SingleUserResponse>> = _registerResult

    private val _users = MutableLiveData<Result<List<UserItem?>>>()
    val users: LiveData<Result<List<UserItem?>>> get() = _users

    private val _usersData = MutableLiveData<Result<UserItem>>()
    val usersData: LiveData<Result<UserItem>> get() = _usersData

    fun register(phone: String, password: String, name: String, address: String, balance: String) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            val result = repository.registerUser(phone, password, name, address, balance)
            _registerResult.value = result
        }
    }

    fun editUser(userId: String, phone: String, name: String, address: String, balance: Double) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            val result = repository.editUser(userId, phone, name, address, balance)
            _registerResult.value = result
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            val result = repository.getUser()
            when (result) {
                is Result.Success -> {
                    _users.postValue(Result.Success(result.data.data ?: emptyList()))
                }

                is Result.Error -> {
                    _users.postValue(Result.Error(result.error))
                    Log.e("ManageCatalogViewModel", "Failed to fetch catalog: ${result.error}")
                }

                Result.Loading -> {}
            }
        }
    }

    fun fetchUserDetails(userId: String) {
        viewModelScope.launch {
            _usersData.value = Result.Loading
            when (val result = repository.getUserById(userId)) {
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

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            _usersData.value = Result.Loading
            when (val result = repository.deleteUser(userId)) {
                is Result.Success -> {
                    result.data
                }
                is Result.Error -> {
                    _usersData.postValue(Result.Error(result.error))
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }
                Result.Loading -> {}
            }
        }
    }

}