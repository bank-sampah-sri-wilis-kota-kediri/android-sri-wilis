package com.bs.sriwilis.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.ui.history.ManageHistoryMutationViewModel
import com.bs.sriwilis.ui.history.ManageHistoryOrderViewModel
import com.bs.sriwilis.ui.scheduling.OrderSchedulingViewModel

class HistoryViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageHistoryOrderViewModel::class.java)) {
            return ManageHistoryOrderViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ManageHistoryMutationViewModel::class.java)) {
            return ManageHistoryMutationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

