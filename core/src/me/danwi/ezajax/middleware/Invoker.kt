package me.danwi.ezajax.middleware

import me.danwi.ezajax.container.Current
import me.danwi.ezajax.util.EzError
import java.lang.reflect.InvocationTargetException

/**
 * 真正的方法调用
 * Created by demon on 2017/2/16.
 */
var Invoker = {
    try {
        val result = Current.method.method.invoke(Current.module.clazz.newInstance(), *Current.args)
        Current.response.returnValue(result)
    } catch (e: IllegalAccessException) {
        throw EzError(-5, "非法访问异常: $e")
    } catch (e: IllegalArgumentException) {
        throw EzError(-5, "非法参数异常: $e")
    } catch (e: InvocationTargetException) {
        if (e.targetException is EzError)
            throw e.targetException
        else
            throw EzError(-5, "方法内部异常: ${e.targetException}")
    }
    false
}