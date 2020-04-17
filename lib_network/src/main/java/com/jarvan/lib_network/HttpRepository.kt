package com.jarvan.lib_network

import com.google.gson.GsonBuilder
import com.jarvan.lib_network.data.Weather
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object HttpRepository {

    /**
     * 数据脱壳与错误预处理
     */
    fun <T> preprocessData(responseBody: APIResponse<T>): T {
        return if (responseBody.status == 0) responseBody.data.data
        else throw Throwable(responseBody.message)
    }

    suspend fun getWeather(): Weather {
        val responseBody = getApiService().getWeather()
        return preprocessData(responseBody)
    }

    open fun getApiService(): APIService {
        return Retrofit.Builder()
            .baseUrl(APIService.API_BASE_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(provideOkHttpClient())
            .build()
            .create(APIService::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.MINUTES)
            readTimeout((60 * 3), TimeUnit.SECONDS)
            addInterceptor(provideLoggingInterceptor())
            addInterceptor(provideHeaderInterceptor())
        }.build()

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.i(it)
        }).apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    private fun provideHeaderInterceptor(): Interceptor =
        Interceptor {
            var timestamp = System.currentTimeMillis().toString()
            val request = it.request().newBuilder()
                .addHeader("timestamp", timestamp)
                .build()
            it.proceed(request)
        }

}