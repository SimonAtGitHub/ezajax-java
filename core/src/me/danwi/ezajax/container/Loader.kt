package me.danwi.ezajax.container

import me.danwi.ezajax.annotation.Ajax


/**
 * 加载器
 * Created by demon on 2017/2/14.
 */

val container: MutableMap<String, AjaxModule> = mutableMapOf()

/**
 * 加载所有的ezajax类模块
 */
fun loadAllClasses() {
    val classNames = scanAllClasses()

    //找出所有的Ajax模块类
    val ajaxClasses = classNames.filter {
        try {
            val clazz = Class.forName(it)
            val annotaion = clazz.getAnnotation(Ajax::class.java)
            annotaion != null
        } catch (e: Exception) {
            false
        }
    }.map { Class.forName(it) }

    //解析ajax类,并把他们添加了容器里面
    ajaxClasses.forEach {
        val module = AjaxModule(it)
        container[module.name] = module
        println("Found Ajax Module: ${it.simpleName}")
    }

    println("All Ajax Modules Loaded")
}

