package com.jarvan.lib_network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val retrofit: Retrofit by lazy {

    val client =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout((60 * 3), TimeUnit.SECONDS)
//            .addNetworkInterceptor(HttpLoggingInterceptor { Timber.i(it) }
//                .setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor {
                var timestamp = System.currentTimeMillis().toString()
                val request = it.request().newBuilder()
                    .addHeader("timestamp", timestamp)
                    .build()
                it.proceed(request)
            }
            .build()

    Retrofit
        .Builder()
        .build()
}
