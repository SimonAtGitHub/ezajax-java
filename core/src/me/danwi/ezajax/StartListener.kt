package me.danwi.ezajax

import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

/**
 * 初始化监听器
 * Created by demon on 2016/10/26.
 */
@WebListener
class StartListener : ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) {
        println("start")
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        println("end")
    }
}