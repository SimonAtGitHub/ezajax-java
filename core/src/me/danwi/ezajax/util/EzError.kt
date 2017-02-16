package me.danwi.ezajax.util

/**
 * EzAjax通用异常
 * Created by demon on 2017/2/16.
 */
class EzError(override val message: String) : Exception(message) {
    var code: Int = 10000
        private set

    constructor(code: Int, message: String) : this(message) {
        this.code = code
    }
}