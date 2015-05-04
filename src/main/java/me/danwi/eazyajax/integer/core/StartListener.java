package me.danwi.eazyajax.integer.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import me.danwi.eazyajax.EazyAjax;
import me.danwi.eazyajax.channel.core.PrivilegeBase;
import me.danwi.eazyajax.container.core.ModuleContainer;
import me.danwi.eazyajax.container.core.PrivilegeContainer;
import me.danwi.eazyajax.integer.utils.TomcatHelper;

/**
 * 核心监听器,用于监听Web程序的启动,在启动的时候做一些EazyAjax的初始化工作
 * @author Demon
 *
 */
@WebListener
public class StartListener implements ServletContextListener {

    /**
     * 卸载方法,用于卸载EazyAjax,做一些清理工作,现在基本没用
     * 在Web程序将关闭时被Tomcat自动调用
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        if(EazyAjax.DEBUG)
            System.out.println("-------------------------------------\nweb程序关闭,卸载EazyAjax");
    }

    /**
     * 初始化方法,用于初始化EazyAjax
     * 在Web程序启动的时被Tomcat自动调用,详情参见  ServletListener
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            //初始化扫描所有类
            TomcatHelper.Scan(arg0);

            //硬编码开启调试模式
            EazyAjax.DEBUG = true;

            if(EazyAjax.DEBUG)
                System.out.println("-------------------------------------\nweb程序启动,初始化EazyAjax");

            //初始化Ajax模块(AjaxModule @see AjaxModule)容器
            ModuleContainer container = new ModuleContainer();
			/*
			 * AjaxModule解释:
			 * 		Ajax模块类,由一个普通的类解析而来,这个普通类是你想暴露给前端JS调用的
			 * 
			 * 		比如,有一个全名为   AjaxPackage.AjaxClass 这样的一个类,这个类是你想
			 * 		暴露给前端调用的,EazyAjax框架会将这个类彻底的分析一遍,生成AjaxModule对象
			 * 		缓存起来,这个AjaxModule对象包含了AjaxClass类的一些信息,这些信息都是EazyAjax
			 * 		框架所需要用到的,缓存起来后,下次就不必分析了
			 */


            if(EazyAjax.DEBUG)
                System.out.println("EazyAjax容器初始化完毕,准备初始化Ajax权限模块");

            if (EazyAjax.DEBUG)
                System.out.println("-------------------------------------\n权限模块开始加载");
            //遍历权限类名数组,初始化每一个Ajax权限类
            for(String privilegeClassName:TomcatHelper.getPrivilegesModules())
            {
                String simpleClassName;
                int lastIndexOfDot = privilegeClassName.lastIndexOf('.');
                if(lastIndexOfDot != -1)
                    simpleClassName = privilegeClassName.substring(lastIndexOfDot+1,privilegeClassName.length());
                else
                    simpleClassName = privilegeClassName;

                PrivilegeContainer.initPrivilege(simpleClassName,privilegeClassName);
                System.out.println(String.format("-------权限模块:%s加载完毕!!", simpleClassName));
            }
            if(EazyAjax.DEBUG)
                System.out.println("所有权限模块加载完毕\n-------------------------------------");

            //遍历类名数组,将每一个需要加载的类初始化为AjaxModule,并添加到容器(@see ModuleContainer)中,便于以后的使用
            for(String className:TomcatHelper.getAjaxModules())
                container.addModule(className);

            //全部加载完毕,EazyAjax初始化工作完成
            if(EazyAjax.DEBUG)
                System.out.println("所有Ajax模块加载完毕\n-------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
