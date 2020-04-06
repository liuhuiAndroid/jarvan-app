package com.jarvan.lib_network.data

import androidx.databinding.BaseObservable
import java.io.Serializable

class Feed : BaseObservable(), Serializable {

    var id = 0

    var url: String = "null"
}