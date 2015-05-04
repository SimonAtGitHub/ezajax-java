package me.danwi.eazyajax.container.objects;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.danwi.eazyajax.EazyAjax;
import me.danwi.eazyajax.container.annotations.AjaxFunc;
import me.danwi.eazyajax.container.annotations.AjaxClass;

public class AjaxModule {
    //模块名
    private String name;
    //对应普通类的实例
    private Object instance = null;
    //线程同步锁
    private Object lock = new Object();
    //对应普通类的类对象
    private Class<?> clazz;
    //AjaxMethod链表
    private List<AjaxMethod> ajaxMethods = new ArrayList<AjaxMethod>();

    //获取AjaxModule指向的Ajax类的实例
    //提供了延时实例化功能,只有在Ajax类中方法第一次被调用的时候才实例化这个类
    public Object getInstance() throws InstantiationException, IllegalAccessException {
        //判断实例是否为空,即判断是否已经实例化
        if(this.instance == null)
        {
            //没有实例化,进入实例化同步块
            synchronized (this.lock) {
                //再一次判断实例是否为空
                if(this.instance == null)
                {
                    //实例化普通类
                    this.instance = this.clazz.newInstance();
                    //输出DEBUG信息
                    if(EazyAjax.DEBUG)
                        System.out.println(this.clazz.getName()+"被实例化");
                }
            }
        }
        return this.instance;
    }

    /**
     * 分析和扫描普通类中的方法,并把他们构造成为AjaxMethod对象,添加到ajaxMethods链表中
     */
    private void scanAndAnalyzeMethod()
    {
        //通过反射获取普通类中声明的方法
        Method[] methods = this.clazz.getDeclaredMethods();

        //遍历分析
        for(Method method:methods)
        {
            //判断该方法是否被AjaxFunc修饰过,即改方法是不是需要暴露的方法
            if(method.getAnnotation(AjaxFunc.class)!=null)
            {
                //TODO:是否允许同名方法,对于重载的支持
                //构造AjaxMethod对象
                AjaxMethod ajaxMethod = new AjaxMethod(method);
                //将AjaxMethod对象的所属的模块只因为当前AjaxModule
                ajaxMethod.ajaxModule = this;
                //将AjaxMethod对象添加
                this.ajaxMethods.add(ajaxMethod);
            }
        }
    }

    /**
     * AjaxModule构造函数
     * 将指定需要暴露给js调用的普通类,构造为EazyAjax框架需要的AjaxModule
     * 所做的工作包括:
     * 		1.检查普通类的合法性,获取普通类的模块名称
     * 		2.扫描普通类的所有方法
     * @param clazz 需要暴露给前端js调用的普通类的类对象
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public AjaxModule(Class<?> clazz) throws InstantiationException, IllegalAccessException
    {
        this.clazz = clazz;
        //this.instance = this.clazz.newInstance();

        //通过反射获取普通类声明的模块名
        AjaxClass ajaxClassAnnotation = this.clazz.getAnnotation(AjaxClass.class);
        this.name = ajaxClassAnnotation.value();
        ////如果AjaxClass修饰时没有指定模块名,则使用普通类的simpleName(不包含包名)
        if(this.name.equals(""))
            this.name = this.clazz.getSimpleName();

        //输出DEBUG信息
        if (EazyAjax.DEBUG)
            System.out.println(String.format("-------------------------------------\n模块:%s开始加载", this.name));

        //扫描分析普通类中的方法
        scanAndAnalyzeMethod();

        //输出DEBUG信息
        if (EazyAjax.DEBUG)
            System.out.println(String.format("模块:%s加载完毕\n-------------------------------------", this.name));
    }

    /**
     * 通过方法名获取AjaxMethod,找不到则返回null
     * @param methodName 方法名
     * @return 指定的AjaxMethod对象
     */
    public AjaxMethod getMethod(String methodName)
    {
        //遍历ajaxMethods链表
        for(AjaxMethod ajaxMethod:this.ajaxMethods)
        {
            if(methodName.equals(ajaxMethod.getName()))
                return ajaxMethod;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AjaxMethod> getMethods() {
        return ajaxMethods;
    }
}
