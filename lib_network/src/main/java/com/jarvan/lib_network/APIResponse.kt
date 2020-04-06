package com.jarvan.lib_network

class APIResponse<T> {
    var status = 0
    var message: String? = null
    var data: InnerData<T>? = null

    class InnerData<T> {
        var data: T? = null
    }
}