package com.jarvan.lib_network

import com.jarvan.lib_network.data.Weather
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface APIService {

    companion object {
        const val API_BASE_SERVER_URL = "https://raw.githubusercontent.com/"
    }

    @GET("zaihuishou/Kotlin-mvvm/master/data.json")
    suspend fun getWeather(): Weather

}