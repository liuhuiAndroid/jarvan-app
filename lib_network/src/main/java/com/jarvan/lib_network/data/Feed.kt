package com.jarvan.lib_network.data

import androidx.databinding.BaseObservable
import java.io.Serializable

/**
 * BaseObservable:双向绑定
 */
class Feed : BaseObservable(), Serializable {

    var id = 0

    var feeds_text: String = "null"

    /**
     * 用来对比两个对象是否相等一致
     * equal一般比较全面比较复杂，这样效率就比较低
     */
    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else {
            if (other !is Feed) {
                false
            } else {
                other.id == this.id && other.feeds_text == this.feeds_text
            }
        }
    }

    /**
     * 用来对比两个对象是否相等一致
     * hashCode效率高但是并不是完全可靠
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }

}