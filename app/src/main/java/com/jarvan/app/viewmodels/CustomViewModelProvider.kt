package com.jarvan.app.viewmodels

import com.jarvan.app.viewmodels.factory.DashboardViewModelFactory
import com.jarvan.lib_network.HttpRepository

/**
 * ViewModel提供者
 */
object CustomViewModelProvider {

    fun providerDashboardViewModel(): DashboardViewModelFactory {
        return DashboardViewModelFactory(HttpRepository.getApiService())
    }

}