package com.bs.sriwilis.ui.homepage.operation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.model.Catalog
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.repository.modelhelper.CardCatalog
import com.bs.sriwilis.data.response.CatalogData
import com.bs.sriwilis.data.response.CatalogResponse
import com.bs.sriwilis.data.response.CatalogResponseDTO
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.room.entity.CatalogEntity
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.launch

class ManageCatalogViewModel(private val repository: MainRepository) : ViewModel() {

    private val _addCatalogResult = MutableLiveData<Result<CatalogResponse>>()
    val addCatalogResult: LiveData<Result<CatalogResponse>> = _addCatalogResult

    private val _catalog = MutableLiveData<Result<List<CardCatalog?>>>()
    val catalog: LiveData<Result<List<CardCatalog?>>> get() = _catalog

    private val _catalogDetail = MutableLiveData<Result<CardCatalog?>>()
    val catalogDetail: LiveData<Result<CardCatalog?>> get() = _catalogDetail

    private val _catalogData = MutableLiveData<Result<CatalogData>>()
    val catalogData: LiveData<Result<CatalogData>> get() = _catalogData

    fun addCatalog(token: String, name: String, desc: String, price: String, number: String, link: String, image: String) {
        viewModelScope.launch {
            _addCatalogResult.value = Result.Loading
            val result = repository.addCatalog(token, name, desc, price, number, link, image)
            _addCatalogResult.value = result
        }
    }

    fun editCatalog(catalogId: String, name: String, desc: String, price: String, number: String, link: String, image: String) {
        viewModelScope.launch {
            _addCatalogResult.value = Result.Loading
            val result = repository.editCatalog(catalogId, name, desc, price, number, link, image)
            _addCatalogResult.value = result
        }
    }

    suspend fun getCatalog() {
        _catalog.postValue(Result.Loading)
        val result = repository.getAllCatalogDao()
        _catalog.postValue(result)
    }

    fun fetchCatalogDetails(id: String) {
        viewModelScope.launch {
            _catalogDetail.value = Result.Loading
            when (val result = repository.getCatalogByIdDao(id)) {
                is Result.Success -> {
                    _catalogDetail.postValue(Result.Success(result.data))
                }
                is Result.Error -> {
                    _catalogDetail.postValue(Result.Error(result.error))
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }
                Result.Loading -> TODO()
            }
        }
    }

    fun deleteCatalog(categoryId: String) {
        viewModelScope.launch {
            _catalogData.value = Result.Loading
            when (val result = repository.deleteCatalog(categoryId)) {
                is Result.Success -> {
                    result.data
                }
                is Result.Error -> {
                    _catalogData.postValue(Result.Error(result.error))
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }
                Result.Loading -> {}
            }
        }
    }

    suspend fun getToken(): String {
        return repository.getToken().toString()
    }

    suspend fun syncData(): Result<Unit> {
        return repository.syncCatalog()
    }
}