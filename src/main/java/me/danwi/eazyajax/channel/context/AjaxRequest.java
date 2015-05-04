package me.danwi.eazyajax.channel.context;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

public class AjaxRequest {
    //线程变量,这里加上后面的getCurrentHttpRequest方法的意义是什么?需理解Servlet的生命周期
	public static final ThreadLocal<HttpServletRequest> httpRequest = new ThreadLocal<HttpServletRequest>();
    public static final ThreadLocal<Object[]> params = new ThreadLocal<Object[]>();

    //Ajax想调用的模块名
	private String moduleName;
    //Ajax想调用的方法名
	private String methodName;
    //方法调用参数json
	private String paramsJson;

    //获取此次Ajax调用的HttpRequest
	public static HttpServletRequest getCurrentHttpRequest() {
		return AjaxRequest.httpRequest.get();
	}

    //获取此次Ajax调用的参数数组
    public static Object[] getParams(){
        return AjaxRequest.params.get();
    }

	public String getModuleName() {
		return moduleName;
	}
	public String getMethodName() {
		return methodName;
	}
	public String getParamsJson() {
		return paramsJson;
	}

    /**
     * 分析ServletRequest,构造AjaxRequest
     * @param request ServletRequest
     */
	public  AjaxRequest(HttpServletRequest request) {
        //这里是什么?
		AjaxRequest.httpRequest.set(request);
		
		String[] subPaths = request.getRequestURL().toString().split("/");
		this.moduleName = subPaths[subPaths.length-2];
        //删除.ac后缀
		this.methodName = subPaths[subPaths.length-1].replaceAll(".ac","");
        this.paramsJson = request.getParameter("args")
                .replaceAll("\\{\\[and\\]\\}", "&")
                .replaceAll("\\{\\[plus\\]\\}", "+")
                .replaceAll("\\{\\[sub\\]\\}", "-")
                .replaceAll("\\{\\[hash\\]\\}", "#");

    }
}
