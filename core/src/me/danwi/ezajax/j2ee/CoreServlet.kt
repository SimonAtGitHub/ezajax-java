package me.danwi.ezajax.j2ee

import me.danwi.ezajax.container.AjaxContext
import me.danwi.ezajax.container._threadLocal
import me.danwi.ezajax.middleware.*
import me.danwi.ezajax.util.EzError
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 核心Servlet,获取所有的请求
 * Created by demon on 2017/2/14.
 */
@WebServlet(urlPatterns = arrayOf("/ezajax/*"))
class CoreServlet : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doPost(req, resp)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        //注入context
        _threadLocal.set(AjaxContext(req!!, resp!!))
        //遍历中间件
        arrayOf(
                ContextInit,
                InvokeChecker,
                ArgsFormat,
                Invoker
        ).forEach {
            try {
                if (!it()) return
            } catch (e: EzError) {
                resp.error(e.code, e.message)
            } catch (e: Exception) {
                resp.error(-1, e.toString())
                return
            }
        }

        //flush一下
        resp.writer.flush()
    }
}