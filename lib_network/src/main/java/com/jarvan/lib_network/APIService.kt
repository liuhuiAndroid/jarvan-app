package com.jarvan.lib_network

import com.jarvan.lib_network.data.Feed
import com.jarvan.lib_network.data.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    companion object {
        const val API_BASE_SERVER_URL = "http://123.56.232.18:8080/serverdemo/"
    }

    @GET("zaihuishou/Kotlin-mvvm/master/data.json")
    suspend fun getWeather(): Weather


    @GET("feeds/queryHotFeedsList")
    fun getHotFeedsList(
        @Query("feedType") feedType: String,
        @Query("feedId") feedId: Int,
        @Query("userId") userId: Int,
        @Query("pageCount") pageCount: Int
    ): Call<APIResponse<List<Feed>>>

}