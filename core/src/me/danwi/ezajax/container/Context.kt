@file:JvmName("Context")

package me.danwi.ezajax.container

import me.danwi.ezajax.annotation.Ajax
import me.danwi.ezajax.annotation.ParamName
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 调用上下文
 * Created by demon on 2017/2/15.
 */
val _threadLocal: ThreadLocal<AjaxContext> = ThreadLocal()

//获取到当前上下文
val Current: AjaxContext
    get() = _threadLocal.get()

data class AjaxContext(val request: HttpServletRequest, val response: HttpServletResponse) {
    lateinit var module: AjaxModule
    lateinit var method: AjaxMethod
    lateinit var args: Array<Any?>
}

class AjaxModule(val clazz: Class<*>) {
    val name: String
    val methods: Map<String, AjaxMethod>

    init {
        //获取到模块名
        val annotation = clazz.getAnnotation(Ajax::class.java)
        this.name = if (annotation.value != "") annotation.value else clazz.simpleName
        //开始解析所有的方法
        this.methods = clazz.declaredMethods
                .filter { Modifier.isPublic(it.modifiers) }
                .map {
                    val method = AjaxMethod(it)
                    Pair(method.name, method)
                }.toMap()
    }

}

class AjaxMethod(val method: Method) {
    val name: String
    val params: List<AjaxParameter>

    init {
        this.name = this.method.name

        //解析所有的参数
        val tempParams = mutableListOf<AjaxParameter>()
        val types = this.method.parameterTypes
        val annotations = this.method.parameterAnnotations

        for (index in types.indices) {
            //获取类型
            var type = types[index]
            //获取名称
            var name = "_arg_$index"
            var paramNameAnnotation = annotations[index].filter { it is ParamName }.map { it as ParamName }
            if (paramNameAnnotation.size > 0)
                name = paramNameAnnotation[0].value
            tempParams += AjaxParameter(type, name)
        }
        params = tempParams
    }
}

data class AjaxParameter(val type: Class<*>, val name: String)