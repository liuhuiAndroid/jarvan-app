package com.jarvan.lib_network

import com.jarvan.lib_network.data.Feed
import com.jarvan.lib_network.data.Weather
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    companion object {
        const val API_BASE_SERVER_URL = "http://123.56.232.18:8080/serverdemo"
    }

    @GET("zaihuishou/Kotlin-mvvm/master/data.json")
    suspend fun getWeather(): Weather


    @GET("/feeds/queryHotFeedsList")
    suspend fun getHotFeedsList(@Path("feedType") feedType: String,
                                @Path("feedId") feedId: Int,
                                @Path("userId") userId: Int,
                                @Path("pageCount") pageCount: Int): APIResponse<List<Feed>>

}