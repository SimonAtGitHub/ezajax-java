package me.danwi.eazyajax.container.objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.danwi.eazyajax.EazyAjax;
import me.danwi.eazyajax.channel.core.PrivilegeBase;
import me.danwi.eazyajax.container.annotations.AjaxFunc;
import me.danwi.eazyajax.container.annotations.AjaxPri;
import me.danwi.eazyajax.container.core.PrivilegeContainer;

public class AjaxMethod {
    //方法名
    private String name;
    //对应普通类中方法的方法对象
    private Method method;
    //所属的AjaxModule
    public AjaxModule ajaxModule;
    //存储权限实例
    private List<PrivilegeBase> privileges = new ArrayList<PrivilegeBase>();

    /**
     * 将普通类中对应的方法的方法对象构造为EazyAjax系统中的AjaxMethod对象
     *
     * @param method 方法对象
     */
    public AjaxMethod(Method method) {
        this.method = method;

        //获取方法名
        AjaxFunc ajaxFuncAnnotation = this.method.getAnnotation(AjaxFunc.class);
        this.name = ajaxFuncAnnotation.value();
        if (this.name.equals(""))
            //如果AjaxFunc修饰时没有指定方法名,则使用普通类中方法本来的名字
            this.name = this.method.getName();


        //填充权限实例链表
        AjaxPri ajaxPriAnnotation = this.method.getAnnotation(AjaxPri.class);
        if (ajaxPriAnnotation != null) {
            for (String privilegeName : ajaxPriAnnotation.value()) {
                this.privileges.add(PrivilegeContainer.getPrivilegeInstance(privilegeName));
            }
        }

        //输出DEBUG信息
        if (EazyAjax.DEBUG)
            System.out.println(String.format("-------方法:%s加载完毕!!", this.name));
    }

    /**
     * 通过反射调用普通类中的方法
     *
     * @param args 需要的参数数组
     * @return 普通类中方法调用的返回值
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public Object invoke(Object args[]) throws Exception {
        if (privileges.size() > 0) {
            for (PrivilegeBase privilege : privileges) {
                if (privilege.hasPrivi()) {
                    return this.method.invoke(this.ajaxModule.getInstance(), args);
                }
            }
            throw new Exception("权限不足");
        } else
            return this.method.invoke(this.ajaxModule.getInstance(), args);
    }

    /**
     * 获取对应普通类中的方法的返回值类型
     *
     * @return 返回值类型
     */
    public Class<?> getReturnType() {
        return this.method.getReturnType();
    }

    /**
     * 获取对应普通类中的方法的所有参数类型
     *
     * @return 参数类型数组
     */
    public Class<?>[] getParamTypes() {
        return this.method.getParameterTypes();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
