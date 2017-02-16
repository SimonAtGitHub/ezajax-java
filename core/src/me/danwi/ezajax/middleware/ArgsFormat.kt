package me.danwi.ezajax.middleware

import me.danwi.ezajax.container.Current
import me.danwi.ezajax.util.JSON

/**
 * 参数格式化/反序列化
 * Created by demon on 2017/2/16.
 */
val ArgsFormat = {

    val method = Current.method
    val request = Current.request
    val params = method.params

    Current.args = params.map {
        val httpValue = request.getParameter(it.name)
        httpValue ?: return@map null

        try {
            JSON.fromJson(httpValue, it.type)
        } catch (e: Exception) {
            null
        }
    }.toTypedArray()

    true
}