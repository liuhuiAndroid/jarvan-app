package com.jarvan.lib_network

class APIResponse<T>(
    val status: Int,
    val message: String,
    val data: InnerData<T>
)

class InnerData<T>(var data: T)