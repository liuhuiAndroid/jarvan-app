package com.jarvan.app.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jarvan.app.viewmodels.DashboardViewModel
import com.jarvan.lib_network.APIService

class DashboardViewModelFactory(private val apiService: APIService) : ViewModelProvider.NewInstanceFactory(){

//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return DashboardViewModel(apiService) as T
//    }
}