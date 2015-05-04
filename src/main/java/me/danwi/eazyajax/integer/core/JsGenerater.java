package me.danwi.eazyajax.integer.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.danwi.eazyajax.EazyAjax;
import me.danwi.eazyajax.container.core.ModuleContainer;
import me.danwi.eazyajax.container.objects.AjaxMethod;
import me.danwi.eazyajax.container.objects.AjaxModule;

public class JsGenerater {
    private static String moduleJs = "";
    private static String coreJs = "";
    public static String getModuleJs()
    {
        //var test = {
        //	    show:function(name,callback)
        //	    {
        //	        callback = callback||undefined;
        //	        return EzayAjax.invoke(EzayAjax.createParams("test","show",callback,1,arguments));
        //	    }
        //	}

        if(moduleJs.equals(""))
        {
            ModuleContainer moduleContainer = new ModuleContainer();
            for(AjaxModule ajaxModule:moduleContainer.getModules())
            {
                String methedsScripts = "";
                for(AjaxMethod ajaxMethod:ajaxModule.getMethods())
                {
                    Class<?>[] paramTypes = ajaxMethod.getMethod().getParameterTypes();
                    int paramNum = paramTypes.length;
                    String paramsList = "";
                    for(int i =0;i<paramNum;i++)
                    {
                        paramsList = paramsList + "arg" + i + ",";
                    }
                    String methedScript = String.format(
                            "%s:function(%scallback){return EzayAjax.invoke(EzayAjax.createParams('%s','%s',callback,%d,arguments));},",
                            ajaxMethod.getName(),paramsList,ajaxModule.getName(),ajaxMethod.getName(),paramNum);
                    methedsScripts += methedScript;
                }
                if(!methedsScripts.equals(""))
                    methedsScripts = methedsScripts.substring(0,methedsScripts.length()-1);
                String moduleScript = 	String.format("var %s={%s}\n",ajaxModule.getName(),methedsScripts);
                moduleJs += moduleScript;
            }
        }
        return moduleJs;
    }

    public static String getCoreJs() throws IOException
    {
        if(coreJs.equals(""))
        {
            String jsFileName = EazyAjax.DEBUG?"core.js":"core_min.js";
            InputStream inputStream  = JsGenerater.class.getClass().getResourceAsStream("me/danwi/eazyajax/integer/res/"+jsFileName);
            if (inputStream == null)
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("me/danwi/eazyajax/integer/res/"+jsFileName);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String content = "";
            StringBuilder sb = new StringBuilder();

            while(content != null)
            {
                content = bufferedReader.readLine();

                if (content == null) {
                    break;
                }
                else {
                    sb.append(content);
                    sb.append('\n');
                }
            }
            bufferedReader.close();
            reader.close();
            inputStream.close();
            coreJs = sb.toString() + getModuleJs();
        }
        return coreJs;
    }
}
