package me.danwi.eazyajax.channel.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Ajax请求上下文
 * @author Demon
 *
 */
public class AjaxContext {
    //不解释
	private AjaxRequest ajaxRequest;
	private AjaxResponse ajaxResponse;
	public AjaxRequest getAjaxRequest() {
		return ajaxRequest;
	}
	public AjaxResponse getAjaxResponse() {
		return ajaxResponse;
	}

    /**
     * 通过传入的 HttpServletRequest和HttpServletResponse初始化AjaxContext
     * @param request 此次Ajax请求的HttpServletRequest
     * @param response 此次Ajax请求的HttpServletResponse
     */
	public  AjaxContext(HttpServletRequest request,HttpServletResponse response) {
		ajaxRequest = new AjaxRequest(request);
		ajaxResponse = new AjaxResponse(response);
	}
}
