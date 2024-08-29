package com.bs.sriwilis.ui.homepage.operation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import com.bs.sriwilis.data.response.UserItem
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result

class ManageUserViewModel(private val repository: MainRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<Result<RegisterUserResponse>>()
    val registerResult: LiveData<Result<RegisterUserResponse>> = _registerResult

    private val _users = MutableLiveData<Result<GetAllUserResponse>>()
    val users: LiveData<Result<GetAllUserResponse>> get() = _users

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
            _users.postValue(result)
        }
    }

    fun fetchUserDetails(userId: String) {
        viewModelScope.launch {
            _usersData.value = Result.Loading
            when (val result = repository.getUserById(userId)) {
                is Result.Success -> {
                    _usersData.postValue(Result.Success(result.data))
                }
                is Result.Error -> {
                    _usersData.postValue(Result.Error(result.error))
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