package me.danwi.eazyajax.integer.utils;

import me.danwi.eazyajax.channel.core.PrivilegeBase;
import me.danwi.eazyajax.container.annotations.AjaxClass;

import javax.servlet.ServletContextEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Demon on 14-3-11.
 */
public class TomcatHelper {
    public static String classesPath = "";

    private static List<String> ajaxModules = new ArrayList<>();
    private static List<String> privilegeModules = new ArrayList<>();

    public static void Scan(ServletContextEvent servletContextEvent) throws Exception {
        classesPath = servletContextEvent.getServletContext().getRealPath("/WEB-INF/classes/");
        //这里的classes路径不同的tomcat中返回的地址是不同的,可能以斜杠结尾,所以这里做一下统一处理

        if (!classesPath.endsWith(File.separator))
            classesPath += File.separator;

        System.out.println("找到classes目录: " + classesPath);

        List<String> classNames = JarUtil.getClassNameByFile(classesPath, true);

        for (String className : classNames) {
            Class clazz = Class.forName(className);
            if (clazz.getAnnotation(AjaxClass.class) != null)
                ajaxModules.add(className);

            Class interfaces[] = clazz.getInterfaces();
            for (Class inter : interfaces) {
                if (inter.getName().equals(PrivilegeBase.class.getName())) {
                    privilegeModules.add(className);
                    break;
                }
            }

        }
    }

    public static List<String> getAjaxModules() {
        return ajaxModules;
    }

    public static List<String> getPrivilegesModules() {
        return privilegeModules;
    }
}
