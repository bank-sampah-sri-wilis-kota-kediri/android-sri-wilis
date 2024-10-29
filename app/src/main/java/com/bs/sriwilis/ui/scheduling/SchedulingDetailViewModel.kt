package com.bs.sriwilis.ui.scheduling

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.CartTransactionRequest
import com.bs.sriwilis.data.response.ChangeResultResponse
import com.bs.sriwilis.data.response.ChangeStatusPesananSampahResponse
import com.bs.sriwilis.data.response.DataKeranjangItem
import com.bs.sriwilis.data.response.GetOrderScheduleByIdResponse
import com.bs.sriwilis.data.response.PesananSampahItem
import com.bs.sriwilis.data.response.PesananSampahKeranjangResponse
import com.bs.sriwilis.data.response.SinglePesananSampahResponse
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransactionResponse
import com.bs.sriwilis.data.response.TransaksiSampahItem
import com.bs.sriwilis.data.response.TransaksiSampahItemResponse
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.model.CartTransaction
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardTransaksi
import kotlinx.coroutines.launch

class SchedulingDetailViewModel(private val repository: MainRepository) : ViewModel() {
    private val _orderSchedule = MutableLiveData<Result<GetOrderScheduleByIdResponse>>()
    val orderSchedule: LiveData<Result<GetOrderScheduleByIdResponse>> = _orderSchedule

    private val _crudResponse = MutableLiveData<Result<SinglePesananSampahResponse>>()
    val crudResponse: LiveData<Result<SinglePesananSampahResponse>> = _crudResponse

    private val _changeResult = MutableLiveData<Result<ChangeResultResponse>>()
    val changeResult: LiveData<Result<ChangeResultResponse>> = _changeResult

    private val _transactionResult = MutableLiveData<Result<TransaksiSampahItemResponse?>>()
    val transactionResult: LiveData<Result<TransaksiSampahItemResponse?>> = _transactionResult

    private val _pesananSampahData = MutableLiveData<List<DataKeranjangItem>>()
    val pesananSampahData: LiveData<List<DataKeranjangItem>> get() = _pesananSampahData

    private val _cartOrderData = MutableLiveData<List<TransactionDataItem>?>()
    val cartOrderData: LiveData<List<TransactionDataItem>?> get() = _cartOrderData

    private val _transactionDataItem = MutableLiveData<Result<List<PesananSampahItem>?>>()
    val transactionDataItem: LiveData<Result<List<PesananSampahItem>?>> get() = _transactionDataItem

    private val _dataKeranjangItem = MutableLiveData<Result<List<DataKeranjangItem>>>()
    val dataKeranjangItem: LiveData<Result<List<DataKeranjangItem>>> get() = _dataKeranjangItem

    private val _pesananSampah = MutableLiveData<Result<CardPesanan>>()
    val pesananSampah: LiveData<Result<CardPesanan>> = _pesananSampah

    private val _transaksiSampah = MutableLiveData<Result<CardTransaksi>>()
    val transaksiSampah: LiveData<Result<CardTransaksi>> = _transaksiSampah

    private val _transaksiSampahDetailList = MutableLiveData<Result<List<CardDetailPesanan>>>()
    val transaksiSampahDetailList: LiveData<Result<List<CardDetailPesanan>>> = _transaksiSampahDetailList

    private val _pesananSampahEntities = MutableLiveData<Result<List<CardPesanan>>>()
    val pesananSampahEntities: LiveData<Result<List<CardPesanan>>> = _pesananSampahEntities // ini untuk semuanya

    private val _pesananSampahDetail = MutableLiveData<Result<List<CardDetailPesanan>>>()
    val pesananSampahDetail: LiveData<Result<List<CardDetailPesanan>>> get() = _pesananSampahDetail // ini untuk card di detail

    private val _deleteResult = MutableLiveData<Result<Boolean>>()
    val deleteResult: LiveData<Result<Boolean>> get() = _deleteResult

    private val _updateCartStatus = MutableLiveData<Result<ChangeStatusPesananSampahResponse>>()
    val updateCartStatus: LiveData<Result<ChangeStatusPesananSampahResponse>>
        get() = _updateCartStatus


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

    fun registerDate(orderId: String, date: String) {
        viewModelScope.launch {
            _crudResponse.postValue(Result.Loading)
            val result = repository.registerDate(orderId, date)
            _crudResponse.postValue(result)
        }
    }

    fun updateFailed(orderId: String, alasanPenolakan: String = "") {
        viewModelScope.launch {
            _crudResponse.postValue(Result.Loading)
            val result = repository.updateOrderFailed(orderId, alasanPenolakan = alasanPenolakan)
            _crudResponse.postValue(result)
        }
    }

    //untuk data list keranjang sampah
    suspend fun getPesananSampahKeranjangList(orderId: String) {
        viewModelScope.launch {
            _pesananSampahDetail.postValue(Result.Loading)
            val result = repository.getPesananSampahKeranjangDetailList(orderId)
            _pesananSampahDetail.postValue(result)
        }
    }

    // untuk data semuanya
    suspend fun getPesananSampahKeranjang() {
        viewModelScope.launch {
            _pesananSampahEntities.postValue(Result.Loading)
            val result = repository.getPesananSampahKeranjang()
            _pesananSampahEntities.postValue(result)
        }
    }

    // untuk data detail
    fun getDataDetailPesananSampahKeranjang(idPesanan: String) {
        viewModelScope.launch {
            _pesananSampah.postValue(Result.Loading)
            val result = repository.getPesananSampahKeranjangDetail(idPesanan)
            _pesananSampah.postValue(result)
        }
    }

    fun getDataDetailTransaksiSampahKeranjang(idPesanan: String) {
        viewModelScope.launch {
            _transaksiSampah.postValue(Result.Loading)
            val result = repository.getTransaksiSampahKeranjangDetail(idPesanan)
            _transaksiSampah.postValue(result)
        }
    }

    fun getTransaksiListDetailById(idPesanan: String) {
        viewModelScope.launch {
            _transaksiSampahDetailList.postValue(Result.Loading)
            val result = repository.getTransaksiDetailListById(idPesanan)
            _transaksiSampahDetailList.postValue(result)
        }
    }

    fun addCartTransaction(
        idNasabah: String,
        tanggal: String,
        cartTransactionList: List<CartTransaction>
    ) {
        viewModelScope.launch {
            _transactionResult.value = Result.Loading

            val result = repository.addCartTransaction(idNasabah, tanggal, cartTransactionList)
            _transactionResult.value = result
        }
    }

    fun deleteKeranjang(keranjangId: String) {
        viewModelScope.launch {
            _deleteResult.postValue(Result.Loading)
            try {
                when (val result = repository.deleteItemKeranjangById(keranjangId)) {
                    is Result.Success -> {
                        if (result.data) {
                            _deleteResult.postValue(Result.Success(true))
                        } else {
                            _deleteResult.postValue(Result.Error("Gagal menghapus keranjang"))
                        }
                    }
                    is Result.Error -> {
                        _deleteResult.postValue(Result.Error(result.error ?: "Unknown error occurred"))
                    }
                    Result.Loading -> {
                    }
                }
            } catch (e: Exception) {
                _deleteResult.postValue(Result.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }

    fun updateCartById(id: String, berat: Float) {
        viewModelScope.launch {
            _updateCartStatus.value = repository.updateCartById(id, berat)
        }
    }

    suspend fun syncDataTransaction(): Result<Unit> {
        return repository.syncTransaksi()
    }

}