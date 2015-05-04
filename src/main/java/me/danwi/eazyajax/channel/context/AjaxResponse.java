package me.danwi.eazyajax.channel.context;

import javax.servlet.http.HttpServletResponse;

import me.danwi.eazyajax.channel.beans.ResultBean;

public class AjaxResponse {
    //线程变量,这里加上后面的getCurrentHttpResponse方法的意义是什么?需理解Servlet的生命周期
	public static final ThreadLocal<HttpServletResponse> httpResponse = new ThreadLocal<HttpServletResponse>();
    //方法调用结果Bean
	private ResultBean result;
	
	public static HttpServletResponse getCurrentHttpResponse() {
		return AjaxResponse.httpResponse.get();
	}
	
	
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}


	public  AjaxResponse(HttpServletResponse response) {
        //这里是什么?
		AjaxResponse.httpResponse.set(response);
	}
}
