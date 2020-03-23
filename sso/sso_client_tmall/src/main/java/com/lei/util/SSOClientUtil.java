package com.lei.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/*跳转到认证中心的一个工具*/
public class SSOClientUtil {

    private static Properties properties = new Properties();
    public static String SERVER_HOST_URL;
    public static String CLIENT_HOST_URL;

    /*在初始化时，读取配置文件*/
    static {
        try {
            /*加载配置文件*/
            properties.load(SSOClientUtil.class.getClassLoader().getResourceAsStream("sso.properties"));
            /*获取值*/
            /*服务器端*/
            SERVER_HOST_URL = properties.getProperty("server.host.url");
            /*客户端*/
            CLIENT_HOST_URL = properties.getProperty("client.host.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*跳转到认证中心*/
    public static void redirectToCheckToken(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer url = new StringBuffer();
        /*读取sso.properties配置文件*/
        /*获取跳转的路径*/
        url.append(SERVER_HOST_URL)
                .append("/checkToken?redirectUrl=")
                .append(CLIENT_HOST_URL)
                .append(request.getServletPath());//这个地方的request.getServletPath()结果就是：/淘宝

        try {
            response.sendRedirect(url.toString());//重定向，到服务器端
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static String getServerLogoutUrl(){
        return  SERVER_HOST_URL+"/logout";
    }

    public static  String getClientLogoutUrl(){
        return CLIENT_HOST_URL+"/logout";
    }
}
