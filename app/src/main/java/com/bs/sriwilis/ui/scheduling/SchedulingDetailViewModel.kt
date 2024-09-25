package com.bs.sriwilis.ui.scheduling

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.DataKeranjangItem
import com.bs.sriwilis.data.response.GetOrderScheduleByIdResponse
import com.bs.sriwilis.data.response.PesananSampahItem
import com.bs.sriwilis.data.response.PesananSampahKeranjangResponse
import com.bs.sriwilis.data.response.SinglePesananSampahResponse
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.launch

class SchedulingDetailViewModel(private val repository: MainRepository) : ViewModel() {
    private val _orderSchedule = MutableLiveData<Result<GetOrderScheduleByIdResponse>>()
    val orderSchedule: LiveData<Result<GetOrderScheduleByIdResponse>> = _orderSchedule

    private val _crudResponse = MutableLiveData<Result<SinglePesananSampahResponse>>()
    val crudResponse: LiveData<Result<SinglePesananSampahResponse>> = _crudResponse

    private val _pesananSampahData = MutableLiveData<Result<List<DataKeranjangItem>>>()
    val pesananSampahData: LiveData<Result<List<DataKeranjangItem>>> get() = _pesananSampahData

    fun registerDate(orderId: String, date: String) {
        viewModelScope.launch {
            _crudResponse.value = Result.Loading
            val result = repository.registerDate(orderId, date)
            _crudResponse.value = result
        }
    }

    fun getPesananSampah() {
        viewModelScope.launch {
            val result = repository.getAllCart()
            when (result) {
                is Result.Success -> {
                    _pesananSampahData.value = Result.Success(result.data.dataKeranjang ?: emptyList())
                }

                is Result.Error -> {
                    _pesananSampahData.value = Result.Error(result.error)
                    Log.e("ManageCatalogViewModel", "Failed to fetch catalog: ${result.error}")
                }
                Result.Loading -> {}
            }
        }
    }

    fun updateSuccess(orderId: String) {
        viewModelScope.launch {
            _crudResponse.value = Result.Loading
            val result = repository.updateOrderSuccess(orderId)
            _crudResponse.value = result
        }
    }

    fun updateFailed(orderId: String) {
        viewModelScope.launch {
            _crudResponse.value = Result.Loading
            val result = repository.updateOrderFailed(orderId)
            _crudResponse.value = result
        }
    }

}