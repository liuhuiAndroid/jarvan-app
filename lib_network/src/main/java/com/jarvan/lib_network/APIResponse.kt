package com.jarvan.lib_network

class APIResponse<T> {
    var success = false
    var status = 0
    var message: String? = null
    var body: T? = null
}