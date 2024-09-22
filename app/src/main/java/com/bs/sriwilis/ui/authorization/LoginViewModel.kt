package com.bs.sriwilis.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.data.repository.AuthRepository
import com.bs.sriwilis.data.response.LoginResponse
import com.bs.sriwilis.data.response.LoginResponseDTO
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponseDTO>>()
    val loginResult: LiveData<Result<LoginResponseDTO>> = _loginResult

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            val result = repository.login(phone, password)
            _loginResult.value = result
        }
    }
}