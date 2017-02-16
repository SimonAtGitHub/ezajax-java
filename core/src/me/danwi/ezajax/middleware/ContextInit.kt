package me.danwi.ezajax.middleware

import com.google.gson.JsonObject
import me.danwi.ezajax.container.Current
import me.danwi.ezajax.util.JSON
import javax.servlet.http.HttpServletResponse

/**
 * 上下文初始化,并添加增强方法
 * Created by demon on 2017/2/14.
 */

/**
 * 上下文初始化中间件
 */
val ContextInit = {
    Current.response.characterEncoding = "utf-8"
    Current.response.contentType = "application/json"
    true
}

/**
 * 增强方法,方便错误返回
 */
fun HttpServletResponse.error(code: Int, message: String) {
    val json = JsonObject()
    json.addProperty("code", code)
    json.addProperty("message", message)
    this.status = 500
    this.writer.print(json.toString())
}

/**
 * 增强方法,方便返回值
 */
fun HttpServletResponse.returnValue(returnValue: Any?) {
    this.status = 200
    this.writer.print(JSON.toJson(returnValue))
}