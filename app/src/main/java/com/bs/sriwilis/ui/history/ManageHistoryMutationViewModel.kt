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
import com.bs.sriwilis.data.response.PenarikanData
import com.bs.sriwilis.data.response.PenarikanResponse
import com.bs.sriwilis.data.response.SingleCategoryResponse
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransactionResponse
import com.bs.sriwilis.data.response.TransaksiSampahItem
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result

class ManageHistoryMutationViewModel(private val repository: MainRepository) : ViewModel() {
    private val _historyMutation = MutableLiveData<Result<List<PenarikanData>?>>()
    val historyMutation: LiveData<Result<List<PenarikanData>?>> get() = _historyMutation

    private val _editMutationResult = MutableLiveData<Result<PenarikanResponse>>()
    val editCategoryResult: LiveData<Result<PenarikanResponse>> = _editMutationResult

    fun updateStatus(mutationId: String, statusPenarikan: String, nomorToken: String? = null) {
        viewModelScope.launch {
            _editMutationResult.value = Result.Loading
            val result = repository.updateStatus(mutationId, statusPenarikan, nomorToken)
            Log.d("update status penarikan",result.toString())
            _editMutationResult.value = result
        }
    }

    fun getAllMutation() {
        viewModelScope.launch {
            when (val result = repository.getAllMutation()) {
                is Result.Success -> {
                    _historyMutation.postValue(Result.Success(result.data.data))
                }
                is Result.Error -> {
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }

                Result.Loading -> TODO()
            }
        }
    }

    fun getCustomerName(userId: String, callback: (String) -> Unit) {
        viewModelScope.launch {
            when (val result = repository.getUserById(userId)) {
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

    fun getCustomerPhone(userId: String, callback: (String) -> Unit) {
        viewModelScope.launch {
            when (val result = repository.getUserById(userId)) {
                is Result.Success -> {
                    result.data.no_hp_nasabah?.let { callback(it) }
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

}