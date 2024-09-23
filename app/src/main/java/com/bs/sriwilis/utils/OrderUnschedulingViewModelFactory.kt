package com.bs.sriwilis.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.ui.scheduling.OrderUnschedulingViewModel

class OrderUnschedulingViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderUnschedulingViewModel::class.java)) {
            return OrderUnschedulingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
