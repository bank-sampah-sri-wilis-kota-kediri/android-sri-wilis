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
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderSchedulingViewModel(private val repository: MainRepository) : ViewModel() {
    private val _scheduledOrdersLiveData = MutableLiveData<List<CardPesanan>?>()
    val scheduledOrdersLiveData: LiveData<List<CardPesanan>?> get() = _scheduledOrdersLiveData

    private val _pesananSampah = MutableLiveData<Result<CardPesanan>>()
    val pesananSampah: LiveData<Result<CardPesanan>> = _pesananSampah // ini untuk detail

    private val _pesananSampahDetail = MutableLiveData<Result<List<CardDetailPesanan>>>()
    val pesananSampahDetail: LiveData<Result<List<CardDetailPesanan>>> get() = _pesananSampahDetail // ini untuk card di detail

    private val _pesananSampahEntities = MutableLiveData<Result<List<CardPesanan>>>()
    val pesananSampahEntities: LiveData<Result<List<CardPesanan>>> = _pesananSampahEntities // ini untuk semuanya

    suspend fun getPesananSampahKeranjangUnscheduled() {
        viewModelScope.launch {
            _pesananSampahEntities.postValue(Result.Loading)
            val result = repository.getPesananSampahKeranjang()

            if (result is Result.Success) {
                // Filter data berdasarkan status_pesanan == "Pending"
                val filteredData = result.data.filter { it.status_pesanan == "Pending" || it.status_pesanan == "Gagal" }
                _pesananSampahEntities.postValue(Result.Success(filteredData))
            } else {
                _pesananSampahEntities.postValue(result)
            }
        }
    }

    suspend fun getPesananSampahKeranjangScheduled() {
        viewModelScope.launch {
            _pesananSampahEntities.postValue(Result.Loading)
            val result = repository.getPesananSampahKeranjang()

            if (result is Result.Success) {
                // Filter data berdasarkan status_pesanan == "Pending"
                val filteredData = result.data.filter { it.status_pesanan == "Sudah Dijadwalkan" || it.status_pesanan == "Selesai diantar"}
                _pesananSampahEntities.postValue(Result.Success(filteredData))
            } else {
                _pesananSampahEntities.postValue(result)
            }
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

    suspend fun syncData(): Result<Unit> {
        return repository.syncData()
    }
}