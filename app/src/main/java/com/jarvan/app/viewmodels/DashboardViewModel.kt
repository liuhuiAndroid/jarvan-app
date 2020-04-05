package com.jarvan.app.viewmodels

import com.jarvan.app.base.BaseLiveData
import com.jarvan.lib_network.HttpRepository
import com.jarvan.lib_network.data.Weather

class DashboardViewModel : BaseViewModel() {

    val repoResult = BaseLiveData<Weather>()

    fun fetchRepos() {
        request(
            execute = {
                val result = HttpRepository.getApiService().getWeather()
                repoResult.update(result)
            }
        )
    }
}