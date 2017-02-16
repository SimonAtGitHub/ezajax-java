package me.danwi.ezajax.middleware

import me.danwi.ezajax.container.Current
import me.danwi.ezajax.container.container
import me.danwi.ezajax.util.EzError

/**
 * 调用中间件
 * Created by demon on 2017/2/14.
 */
val InvokeChecker = {
    val req = Current.request
    val res = Current.response
    val matchResult = Regex("\\/ezajax\\/(\\S+)\\/(\\S+)\\.ac").find(req.requestURI)
    if (matchResult != null) {
        val moduleName = (matchResult.groups[1] ?: throw EzError(-2, "调用路径解析错误")).value
        val methodName = (matchResult.groups[2] ?: throw EzError(-2, "调用路径解析错误")).value

        Current.module = container[moduleName] ?: throw EzError(-2, "模块 $moduleName 找不到")
        Current.method = Current.module.methods[methodName] ?: throw EzError(-2, "方法 $moduleName.$methodName 找不到")
        true
    } else {
        res.error(-2, "非法的请求路径:${req.requestURI}")
        false
    }
}
