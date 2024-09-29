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
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransactionResponse
import com.bs.sriwilis.data.response.TransaksiSampahItem
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.launch

class SchedulingDetailViewModel(private val repository: MainRepository) : ViewModel() {
    private val _orderSchedule = MutableLiveData<Result<GetOrderScheduleByIdResponse>>()
    val orderSchedule: LiveData<Result<GetOrderScheduleByIdResponse>> = _orderSchedule

    private val _crudResponse = MutableLiveData<Result<SinglePesananSampahResponse>>()
    val crudResponse: LiveData<Result<SinglePesananSampahResponse>> = _crudResponse

    private val _pesananSampahData = MutableLiveData<List<DataKeranjangItem>>()
    val pesananSampahData: LiveData<List<DataKeranjangItem>> get() = _pesananSampahData

    private val _cartOrderData = MutableLiveData<List<TransactionDataItem>?>()
    val cartOrderData: LiveData<List<TransactionDataItem>?> get() = _cartOrderData

    private val _transactionDataItem = MutableLiveData<Result<List<PesananSampahItem>?>>()
    val transactionDataItem: LiveData<Result<List<PesananSampahItem>?>> get() = _transactionDataItem

    private val _dataKeranjangItem = MutableLiveData<Result<List<DataKeranjangItem>>>()
    val dataKeranjangItem: LiveData<Result<List<DataKeranjangItem>>> get() = _dataKeranjangItem

    fun fetchSchedule(id: String) {
        viewModelScope.launch {
            when (val result = repository.getOrderCartByNasabahId(id)) {
                is Result.Success -> {
                    _cartOrderData.postValue(result.data.data)
                    Log.d("fetchSchedule", "Data: ${result.data}") // Log the data
                }
                is Result.Error -> {
                    Log.e("FetchUser", "Failed to fetch user details: ${result.error}")
                }
                Result.Loading -> TODO()
            }
        }
    }

    fun fetchTransactionItemById(id: String) {
        viewModelScope.launch {
            when (val result = repository.getTransactionDataItem(id)) {
                is Result.Success -> {
                    _transactionDataItem.postValue(Result.Success(result.data.data))
                    Log.d("fetchTransaction", "Data: ${result.data}") // Log the data
                }
                is Result.Error -> {
                    Log.e("FetchTransaction", "Failed to fetch user details: ${result.error}")
                }
                Result.Loading -> TODO()
            }
        }
    }

    fun fetchDataKeranjangById(id: String) {
        viewModelScope.launch {
            when (val result = repository.getDataKeranjangItem(id)) {
                is Result.Success -> {
                    _dataKeranjangItem.postValue(Result.Success(result.data.data))
                    Log.d("fetchKeranjang", "Data: ${result.data}") // Log the data
                }
                is Result.Error -> {
                    Log.e("FetchKeranjang", "Failed to fetch user details: ${result.error}")
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

    fun getCustomerAddress(userId: String, callback: (String) -> Unit) {
        viewModelScope.launch {
            when (val result = repository.getUserById(userId)) {
                is Result.Success -> {
                    result.data.alamat_nasabah?.let { callback(it) }
                }
                is Result.Error -> {
                    callback("Unknown Customer")
                }
                Result.Loading -> TODO()
            }
        }
    }

    fun registerDate(orderId: String, date: String) {
        viewModelScope.launch {
            _crudResponse.value = Result.Loading
            val result = repository.registerDate(orderId, date)
            _crudResponse.value = result
        }
    }

    fun updateSudahDijadwalkan(orderId: String) {
        viewModelScope.launch {
            _crudResponse.value = Result.Loading
            val result = repository.updateSudahDijadwalkan(orderId)
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