package com.bs.sriwilis.ui.scheduling

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.AdminData
import com.bs.sriwilis.data.response.AdminResponse
import com.bs.sriwilis.data.response.DataKeranjangItem
import com.bs.sriwilis.data.response.PesananSampahKeranjangResponse
import com.bs.sriwilis.data.response.SingleAdminResponse
import com.bs.sriwilis.data.response.SinglePesananSampahResponse
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderSchedulingViewModel(private val repository: MainRepository) : ViewModel() {
    private val _scheduledOrdersLiveData = MutableLiveData<List<DataKeranjangItem>?>()
    val scheduledOrdersLiveData: LiveData<List<DataKeranjangItem>?> get() = _scheduledOrdersLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun fetchScheduledOrders() {
        viewModelScope.launch {
            when (val result = repository.getAllOrderSchedule()) {
                is Result.Success -> {
                    val filteredOrders = result.data.dataKeranjang?.filter { it.statusPesanan == "Sudah Dijadwalkan" }
                    _scheduledOrdersLiveData.postValue(filteredOrders)
                }
                is Result.Error -> _errorLiveData.postValue(result.error)
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
}