package me.danwi.eazyajax.integer.core;

import me.danwi.eazyajax.channel.beans.ResultBean;
import me.danwi.eazyajax.channel.context.AjaxContext;
import me.danwi.eazyajax.channel.core.AjaxDispatcher;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;
import net.sf.json.processors.JsDateJsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet(name = "CoreServlet", urlPatterns = {"/eazyajax/*"})
public class CoreServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //设置编码集防止乱码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        //获取请求url
        String url = req.getRequestURL().toString();

        PrintWriter out = resp.getWriter();

        //分析请求
        if (url.endsWith("core.js")) {
            /*
             * 生成并输出核心js
			 * js包含了与该Servlet互交的方法,给js提供与java无缝调用的环境
			 * 包括请求构造,参数转化等,详情参见 res/core.js文件
			 */
            resp.setHeader("content-type", "text/javascript;charset=UTF-8");
            out.print(JsGenerater.getCoreJs());
        } else {
            resp.setHeader("content-type", "text/html;charset=UTF-8");
            //方法调用请求

            //实例化一个Ajax上下文对象
            AjaxContext ajaxContext = new AjaxContext(req, resp);
            //将实例化的Ajax上下文传递给Ajax调用分发器处理
            AjaxDispatcher dispatcher = new AjaxDispatcher();
            dispatcher.service(ajaxContext);

            //Ajax调用处理完毕,获取处理结果
            ResultBean result = ajaxContext.getAjaxResponse().getResult();

            JsonConfig config = new JsonConfig();
            config.setIgnoreDefaultExcludes(false);
            config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

            config.registerJsonValueProcessor(Date.class, new JsDateJsonValueProcessor());
            config.registerJsonBeanProcessor(Date.class, new JsDateJsonBeanProcessor());


            //将结果解析为json字符串并输出
            JSONObject jsonObject = JSONObject.fromObject(result, config);
            out.print(jsonObject);
        }
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
