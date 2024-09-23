package com.bs.sriwilis.ui.homepage.operation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.repository.modelhelper.CardCategory
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.SingleCategoryResponse
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result

class ManageCategoryViewModel(private val repository: MainRepository) : ViewModel() {

    private val _addCategoryResult = MutableLiveData<Result<SingleCategoryResponse>>()
    val addCategoryResult: LiveData<Result<SingleCategoryResponse>> = _addCategoryResult

    private val _editCategoryResult = MutableLiveData<Result<SingleCategoryResponse>>()
    val editCategoryResult: LiveData<Result<SingleCategoryResponse>> = _editCategoryResult

    private val _categories = MutableLiveData<Result<List<CardCategory>?>>()
    val categories: LiveData<Result<List<CardCategory>?>> get() = _categories

    private val _categoryData = MutableLiveData<Result<CardCategory>>()
    val categoryData: LiveData<Result<CardCategory>> get() = _categoryData

    fun addCategory(name: String, price: String, type: String, imageBase64: String) {
        viewModelScope.launch {
            _addCategoryResult.value = Result.Loading
            val result = repository.addCategory(name, price, type, imageBase64)
            _addCategoryResult.value = result
        }
    }

    fun editCategory(userId: String, name: String, price: String, type: String, image: String) {
        viewModelScope.launch {
            _editCategoryResult.value = Result.Loading
            val result = repository.editCategory(userId, name, price, type, image)
            Log.d("status edit kategori",result.toString())
            _editCategoryResult.value = result
        }
    }

    suspend fun getCategory() {
        _categories.postValue(Result.Loading)
        val result = repository.getAllCategoriesDao()
        _categories.postValue(result)
    }

    fun fetchCategoryDetails(id: String) {
        viewModelScope.launch {
            _categoryData.value = Result.Loading
            when (val result = repository.getCategoryByIdDao(id)) {
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

    fun deleteCategory(categoryId: String) {
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

    suspend fun syncData(): Result<Unit> {
        return repository.syncData()
    }

}