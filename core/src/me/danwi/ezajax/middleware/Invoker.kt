package me.danwi.ezajax.middleware

import me.danwi.ezajax.container.Current

/**
 * 真正的方法调用
 * Created by demon on 2017/2/16.
 */
var Invoker = {
    val result = Current.method.method.invoke(Current.module.clazz.newInstance(), *Current.args)
    Current.response.returnValue(result)
    false
}