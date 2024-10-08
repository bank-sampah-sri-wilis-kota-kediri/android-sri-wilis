package com.bs.sriwilis.ui.history

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
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransactionResponse
import com.bs.sriwilis.data.response.TransaksiSampahItem
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result
import com.bs.sriwilispetugas.data.repository.modelhelper.CardStatus
import com.bs.sriwilispetugas.data.repository.modelhelper.CardTransaksi

class ManageHistoryOrderViewModel(private val repository: MainRepository) : ViewModel() {


    private val _historyOrders = MutableLiveData<Result<List<CardTransaksi>?>>()
    val historyOrders: LiveData<Result<List<CardTransaksi>?>> get() = _historyOrders

    private val _resultHistoryOrders = MutableLiveData<Result<List<CardTransaksi>?>>()
    val resultHistoryOrders: LiveData<Result<List<CardTransaksi>?>> get() = _resultHistoryOrders

    private val _editCategoryResult = MutableLiveData<Result<SingleCategoryResponse>>()
    val editCategoryResult: LiveData<Result<SingleCategoryResponse>> = _editCategoryResult

    private val _categories = MutableLiveData<Result<List<CardCategory>?>>()
    val categories: LiveData<Result<List<CardCategory>?>> get() = _categories

    private val _categoryData = MutableLiveData<Result<CardCategory>>()
    val categoryData: LiveData<Result<CardCategory>> get() = _categoryData

    private val _statusOrder = MutableLiveData<Result<CardStatus>>()
    val statusOrder: LiveData<Result<CardStatus>> get() = _statusOrder

    fun editCategory(userId: String, name: String, price: String, type: String, image: String) {
        viewModelScope.launch {
            _editCategoryResult.value = Result.Loading
            val result = repository.editCategory(userId, name, price, type, image)
            Log.d("status edit kategori",result.toString())
            _editCategoryResult.value = result
        }
    }

    fun getCombinedTransaction() {
        viewModelScope.launch {
            when (val result = repository.getCombinedTransaksiData()) {
                is Result.Success -> {
                    _historyOrders.postValue(Result.Success(result.data))
                }
                is Result.Error -> {
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }

                Result.Loading -> TODO()
            }
        }
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

    fun getCustomerName(userId: String, callback: (String) -> Unit) {
        viewModelScope.launch {
            when (val result = repository.getNasabahById(userId)) {
                is Result.Success -> {
                    result.data.nama_nasabah?.let { callback(it) }
                }
                is Result.Error -> {
                    callback("Unknown Customer")
                }

                Result.Loading -> TODO()
            }
        }
    }

    suspend fun syncData(): Result<Unit> {
        return repository.syncData()
    }

    suspend fun syncDataTransaction(): Result<Unit> {
        return repository.syncTransaksi()
    }

}