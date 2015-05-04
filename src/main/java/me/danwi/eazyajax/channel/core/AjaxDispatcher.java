package me.danwi.eazyajax.channel.core;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import me.danwi.eazyajax.channel.beans.ResultBean;
import me.danwi.eazyajax.channel.context.AjaxContext;
import me.danwi.eazyajax.channel.context.AjaxRequest;
import me.danwi.eazyajax.channel.context.AjaxResponse;
import me.danwi.eazyajax.container.core.ModuleContainer;
import me.danwi.eazyajax.container.objects.AjaxMethod;

/**
 * Ajax调用分发服务
 * @author Demon
 *
 */
public class AjaxDispatcher {
    /**
     * Ajax调用分发服务
     * @author Demon
     *
     */
	public void service(AjaxContext ajaxContext)
	{
		try
		{
            //获取AjaxRequest和AjaxResponse对象
			AjaxRequest ajaxRequest = ajaxContext.getAjaxRequest();
			AjaxResponse ajaxResponse = ajaxContext.getAjaxResponse();

            //获取此次Ajax调用 想要调用的 模块名和方法名
			String moduleName = ajaxRequest.getModuleName();
			String methodName = ajaxRequest.getMethodName();

            //通过模块名和方法名获取 AjaxMethod对象
			ModuleContainer container = new ModuleContainer();
			AjaxMethod ajaxMethod = container.getMethod(moduleName,methodName);

            //判断是否成功获取
			if(ajaxMethod != null)
			{
                //获取此次Ajax调用传过来的参数json
				String parmsJson = ajaxRequest.getParamsJson();
                //解析参数json为JsonArray对象
				JSONArray jsonArray = JSONArray.fromObject(parmsJson);
                //获取AjaxMethod所需要的参数类型数组
				Class<?>[] paramTypes = ajaxMethod.getParamTypes();
                //获取参数的个数
				int paramCount = jsonArray.size();
				Object[] params = new Object[paramCount];
                //构造参数数组,为接下来AjaxMethod的invoke调用做准备
				for (int i = 0; i < paramTypes.length; i++) {
                    //TODO:参数的构造要支持得更加复杂
					Class<?> paramType = paramTypes[i];
					if( jsonArray.get(i).getClass().getName().equals("net.sf.json.JSONObject"))
					{
						params[i] = JSONObject.toBean(jsonArray.getJSONObject(i), paramType);
					}
					else {
						params[i] = jsonArray.get(i);
					}
				}

                //将params同样封装到AjaxRequest的params数组中,以提供公共的参数获取点
                AjaxRequest.params.set(params);
				
				/*
				 * 准备方法调用,前端一个js函数调用所引发的了一系列处理都是为了这一个调用而准备的
				 * 终于到头了
				 */
                //创建调用结果Bean
				ResultBean result = new ResultBean();
                //调用方法,并把方法的结果设置到结果Bean中
				result.setReturnValue(ajaxMethod.invoke(params));
                //把结果Bean放入AjaxResponse中
				ajaxResponse.setResult(result);

                //搞定,nice!!!
			}
		}
		catch(Exception exception)
		{
            //如果出错,构造不同的ResultBean并放入AjaxResponse
			ResultBean result = new ResultBean();
			result.setException(exception);
			ajaxContext.getAjaxResponse().setResult(result);
		}
	}
}
