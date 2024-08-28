package com.bs.sriwilis.ui.homepage.operation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.UserItem
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result

class ManageCategoryViewModel(private val repository: MainRepository) : ViewModel() {

    private val _addCategoryResult = MutableLiveData<Result<CategoryResponse>>()
    val addCategoryResult: LiveData<Result<CategoryResponse>> = _addCategoryResult

    fun addCategory(token: String, name: String, price: String, type: String, imageBase64: String) {
        viewModelScope.launch {
            _addCategoryResult.value = Result.Loading
            val result = repository.addCategory(token, name, price, type, imageBase64)
            _addCategoryResult.value = result
        }
    }

}