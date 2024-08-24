package com.bs.sriwilis.ui.homepage.operation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result

class ManageUserViewModel(private val repository: MainRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<Result<RegisterUserResponse>>()
    val registerResult: LiveData<Result<RegisterUserResponse>> = _registerResult

    private val _users = MutableLiveData<Result<GetAllUserResponse>>()
    val users: LiveData<Result<GetAllUserResponse>> get() = _users


    fun register(phone: String, password: String, name: String, address: String, balance: String) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            val result = repository.registerUser(phone, password, name, address, balance)
            _registerResult.value = result
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            val result = repository.getUser()
            _users.postValue(result)
        }
    }
}