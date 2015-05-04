package me.danwi.eazyajax.container.core;

import java.util.ArrayList;
import java.util.List;

import me.danwi.eazyajax.container.annotations.AjaxClass;
import me.danwi.eazyajax.container.objects.AjaxMethod;
import me.danwi.eazyajax.container.objects.AjaxModule;

/**
 * AjaxModule容器类
 * 用于管理AjaxModule(Ajax模块)
 * @author Demon
 *
 */
public class ModuleContainer {
    //AjaxModule缓存链表,全局静态变量
    private static List<AjaxModule> modulesList = new ArrayList<AjaxModule>();

    /**
     * 将一个类初始化为AjaxModule,并将它添加到容器中
     * @param clazz	这个类的类对象
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void addModule(Class<?> clazz) throws InstantiationException, IllegalAccessException
    {
        //获取clazz的annotation,指定类型为 AjaxClass
        AjaxClass ajaxClassAnnotation = clazz.getAnnotation(AjaxClass.class);
        //判断AjaxClass变量是否为空,即判断clazz代表的类是否经过AjaxClass的修饰
        if(ajaxClassAnnotation!=null)
        {
            //类经过AjaxClass的修饰,表示这个类是需要暴露的类
            //通过clazz生成AjaxModule对象
            AjaxModule ajaxModule = new AjaxModule(clazz);

            //TODO:是否允许同名模块?
            //将ajaxModule添加到链表中
            modulesList.add(ajaxModule);
        }
    }

    /**
     * 将一个类初始化为AjaxModule,并将它添加到容器中
     * @param fullName 这个类的全名
     * @throws ClassNotFoundException 类型未找到,如果出现该错误,请检查需要被加载的类所在的Jar包是否配置正确
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void addModule(String fullName) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        //通过类全名反射得到这个类的Class对象
        //注意,该处可能引发ClassNotFoundException异常
        Class<?> clazz = Class.forName(fullName);
        //调用重载的addModule方法
        addModule(clazz);
    }

    /**
     * 获取容器中所有的AjaxModule
     * @return 所有AjaxModule链表
     */
    public List<AjaxModule> getModules()
    {
        return modulesList;
    }

    /**
     * 通过模块名获取缓存的AjaxModule对象
     * @param moduleName 模块名
     * @return AjaxModule对象,如果没有找到则返回null
     */
    public AjaxModule getModule(String moduleName)
    {
        //遍历链表,获取对象
        for(AjaxModule module:modulesList)
        {
            if(module.getName().equals(moduleName))
                return module;
        }
        //没有找到指定名称的AjaxModule对象,返回空
        return null;
    }

    /**
     * 通过模块名和方法名获取AjaxMethod方法
     * @param moduleName 模块名
     * @param methodName 方法名
     * @return AjaxMethod对象
     */
    public AjaxMethod getMethod(String moduleName,String methodName)
    {
        AjaxModule module = getModule(moduleName);
        if(module != null)
            return module.getMethod(methodName);
        else {
            return null;
        }
    }
}
