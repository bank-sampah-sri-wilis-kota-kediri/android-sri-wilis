package com.bs.sriwilis.ui.homepage.operation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.CategoryResponse
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result

class ManageCategoryViewModel(private val repository: MainRepository) : ViewModel() {

    private val _addCategoryResult = MutableLiveData<Result<CategoryResponse>>()
    val addCategoryResult: LiveData<Result<CategoryResponse>> = _addCategoryResult

    private val _editCategoryResult = MutableLiveData<Result<CategoryResponse>>()
    val editCategoryResult: LiveData<Result<CategoryResponse>> = _editCategoryResult

    private val _categories = MutableLiveData<Result<CategoryResponse>>()
    val categories: LiveData<Result<CategoryResponse>> get() = _categories

    private val _categoryData = MutableLiveData<Result<CategoryData>>()
    val categoryData: LiveData<Result<CategoryData>> get() = _categoryData

    fun addCategory(token: String, name: String, price: String, type: String, imageBase64: String) {
        viewModelScope.launch {
            _editCategoryResult.value = Result.Loading
            val result = repository.addCategory(token, name, price, type, imageBase64)
            _editCategoryResult.value = result
        }
    }

    fun editCategory(userId: String, name: String, price: String, type: String, image: String) {
        viewModelScope.launch {
            _editCategoryResult.value = Result.Loading
            val result = repository.editCategory(userId, name, price, type, image)
            _editCategoryResult.value = result
        }
    }

    fun getCategory() {
        viewModelScope.launch {
            val result = repository.getCategory()
            _categories.postValue(result)
        }
    }

    fun fetchCategoryDetails(id: String) {
        viewModelScope.launch {
            _categoryData.value = Result.Loading
            when (val result = repository.getCategoryById(id)) {
                is Result.Success -> {
                    _categoryData.postValue(Result.Success(result.data))
                }
                is Result.Error -> {
                    _categoryData.postValue(Result.Error(result.error))
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }

                Result.Loading -> TODO()
            }
        }
    }

    fun deleteUser(categoryId: String) {
        viewModelScope.launch {
            _categoryData.value = Result.Loading
            when (val result = repository.deleteCategory(categoryId)) {
                is Result.Success -> {
                    result.data
                }
                is Result.Error -> {
                    _categoryData.postValue(Result.Error(result.error))
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }
                Result.Loading -> {}
            }
        }
    }
}