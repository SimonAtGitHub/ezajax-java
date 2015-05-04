package me.danwi.eazyajax.integer.core;

import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AjaxConfig {
    //是否启用了debug
    private boolean debug = false;
    //配置好的模块名模块名
    private String[] ajaxModules;
    //权限映射表
    private String[] ajaxPrivis;

    public AjaxConfig(String path)
    {
        try{
			/*
			 * 解析Xml文件,获取所有需要加载的类名
			 *
			 * Xml的基本形式如下:
			 *
			 * 		<?xml version="1.0" encoding="UTF-8"?>
			 *		<modules>
			 *			<module>需要加载为AjaxModule的类的全名</module>
			 *		</modules>
			 *		<debug></debug>
			 *
			 *
			 * 默认位置放在 " Web根目录/WEB-INF/eazyajax.xml"
			 */
            //构造需要的DocumentBuilderFactory等
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(new File(path));

            //解析debug开关
            NodeList debugNodeList = dom.getElementsByTagName("debug");
            if(debugNodeList.getLength()>0){
                Node node = debugNodeList.item(0);
                String debug = node.getFirstChild().getNodeValue();
                this.debug = "true".equals(debug);
            }
            else {
                this.debug = false;
            }

            //解析module类
            NodeList moduleNodeList = dom.getElementsByTagName("module");
            String[] modules = new String[moduleNodeList.getLength()];
            for (int i = 0; i < moduleNodeList.getLength(); i++) {
                Node node = moduleNodeList.item(i);
                modules[i] = node.getFirstChild().getNodeValue();
            }
            this.ajaxModules = modules;

            //解析Ajax权限类
            NodeList privilegesNodeList = dom.getElementsByTagName("privilege");
            String[] privileges = new String[privilegesNodeList.getLength()];
            for (int i = 0; i < privilegesNodeList.getLength(); i++) {
                Node node = privilegesNodeList.item(i);
                privileges[i] = node.getFirstChild().getNodeValue();
            }
            this.ajaxPrivis = privileges;

        }
        catch(Exception exception)
        {
            System.out.println("xml配置文件解析错误");
            this.ajaxModules = new String[0];
            this.debug = false;
        }
    }

    public String[] getAjaxModules() {
        return ajaxModules;
    }

    public void setAjaxModules(String[] ajaxModules) {
        this.ajaxModules = ajaxModules;
    }

    @Override
    public String toString() {
        return "AjaxConfig [debug=" + debug + ", modules="
                + Arrays.toString(ajaxModules) + "]";
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }


    public String[] getAjaxPrivis() {
        return ajaxPrivis;
    }
}
