package me.danwi.ezajax.j2ee

import me.danwi.ezajax.container.loadAllClasses
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
        //启动就开始加载所有的类
        loadAllClasses()
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {}
}