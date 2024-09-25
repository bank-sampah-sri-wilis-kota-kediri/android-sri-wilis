package com.bs.sriwilis.ui.homepage.operation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.repository.modelhelper.CardCategory
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

    private val _idList = MutableLiveData<Result<List<String>>>()
    val phoneList: LiveData<Result<List<String>>> get() = _idList

    private val _categories = MutableLiveData<Result<List<Category>>>()
    val categories: LiveData<Result<List<Category>>> get() = _categories

    private val _categoryNames = MutableLiveData<Result<List<String>>>()
    val categoryNames: LiveData<Result<List<String>>> get() = _categoryNames

    data class Category(val name: String, val basePrice: Float)

    fun getUserId() {
        viewModelScope.launch {
            _nasabah.postValue(Result.Loading)
            val result = repository.getAllNasabahDao()
            if (result is Result.Success) {
                val nameList = result.data.map { it.nama_nasabah ?: "Unknown" }
                val idList = result.data.map { it.id }


                _nasabah.postValue(Result.Success(nameList))
                _idList.postValue(Result.Success(idList))
            } else if (result is Result.Error) {
                _nasabah.postValue(Result.Error(result.error))
            }
        }
    }

    fun getIdByPosition(position: Int): String? {
        val result = _idList.value
        return when (result) {
            is Result.Success -> result.data.getOrNull(position)
            is Result.Error -> {
                null
            }
            Result.Loading -> null
            null -> TODO()
        }
    }

    fun getCategoryDetails() {
        viewModelScope.launch {
            _categories.postValue(Result.Loading)
            val result = repository.getAllCategoriesDao()
            if (result is Result.Success) {
                val categoryList = result.data.map { categoryItem ->
                    Category(
                        name = categoryItem.nama_kategori ?: "Unknown",
                        basePrice = categoryItem.harga_kategori?.toFloat() ?: 0.0f
                    )
                }
                _categories.postValue(Result.Success(categoryList))

                val categoryNames = categoryList.map { it.name }
                _categoryNames.postValue(Result.Success(categoryNames))
            } else if (result is Result.Error) {
                _categories.postValue(Result.Error(result.error))
            }
        }
    }



    fun getBasePriceByCategory(categoryName: String): Float? {
        val result = _categories.value
        return when (result) {
            is Result.Success -> {
                val category = result.data.find { it.name == categoryName }
                category?.basePrice
            }
            is Result.Error -> {
                Log.e("ViewModel", "Error fetching categories: ${result.error}")
                null
            }
            Result.Loading -> null
            null -> null
        }
    }


    fun getCategoryByPosition(position: Int): Category? {
        val result = _categories.value
        return when (result) {
            is Result.Success -> result.data.getOrNull(position)
            is Result.Error -> null
            Result.Loading -> null
            null -> null
        }
    }

    suspend fun syncData(): Result<Unit> {
        return repository.syncData()
    }

}