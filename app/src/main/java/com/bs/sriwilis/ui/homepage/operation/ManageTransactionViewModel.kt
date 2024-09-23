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

class ManageTransactionViewModel(private val repository: MainRepository) : ViewModel() {

    private val _nasabah = MutableLiveData<Result<List<String>>>()
    val nasabah: LiveData<Result<List<String>>> get() = _nasabah

    private val _users = MutableLiveData<Result<GetUserByIdResponse>>()
    val users: LiveData<Result<GetUserByIdResponse>> get() = _users

    private val _usersData = MutableLiveData<Result<CardNasabah>>()
    val usersData: LiveData<Result<CardNasabah>> get() = _usersData

    private val _deleteResult = MutableLiveData<Result<Boolean>>()
    val deleteResult: LiveData<Result<Boolean>> get() = _deleteResult

    private val _phoneList = MutableLiveData<Result<List<String>>>()
    val phoneList: LiveData<Result<List<String>>> get() = _phoneList

    fun getUserPhones() {
        viewModelScope.launch {
            _nasabah.postValue(Result.Loading)
            val result = repository.getAllNasabahDao()
            if (result is Result.Success) {
                val nameList = result.data.map { it.nama_nasabah ?: "Unknown" }
                val phoneList = result.data.map { it.no_hp_nasabah }


                _nasabah.postValue(Result.Success(nameList))
                _phoneList.postValue(Result.Success(phoneList))
            } else if (result is Result.Error) {
                _nasabah.postValue(Result.Error(result.error))
            }
        }
    }

    fun getPhoneByPosition(position: Int): String? {
        val result = _phoneList.value
        return when (result) {
            is Result.Success -> result.data.getOrNull(position)
            is Result.Error -> {
                null
            }
            Result.Loading -> null
            null -> TODO()
        }
    }


    suspend fun syncData(): Result<Unit> {
        return repository.syncData()
    }

}