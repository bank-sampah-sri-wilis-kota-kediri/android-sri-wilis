package com.bs.sriwilis.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bs.sriwilis.helper.InjectionAuth
import com.bs.sriwilis.helper.InjectionMain
import com.bs.sriwilis.ui.authorization.LoginViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageCatalogViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageUserViewModel
import com.bs.sriwilis.ui.settings.SettingViewModel
import com.bs.sriwilis.ui.splashscreen.WelcomeViewModel

class ViewModelFactory private constructor(
    private val context: Context,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                val repository = InjectionAuth.provideRepository(context)
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                WelcomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                SettingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ManageUserViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                ManageUserViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ManageCategoryViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                ManageCategoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ManageCatalogViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                ManageCatalogViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(context).also { instance = it }
            }
    }
}
