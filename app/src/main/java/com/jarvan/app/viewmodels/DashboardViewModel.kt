package com.jarvan.app.viewmodels

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.jarvan.app.base.BaseLiveData
import com.jarvan.lib_network.HttpRepository
import com.jarvan.lib_network.data.Weather
import com.jarvan.lib_network.launch

class DashboardViewModel :ViewModel() {

    val repoResult = BaseLiveData<Weather>()

    fun fetchRepos() {
        launch({
            val result = HttpRepository.getWeather()
            repoResult.update(result)
        },{
            it.printStackTrace()
        })
    }
}