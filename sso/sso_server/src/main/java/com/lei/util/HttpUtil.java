package com.lei.util;


import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/*向服务器端发起一个异步请求*/
public class HttpUtil {

    /*
     * id=1
     * name=tom
     * {id=1,name=tom},将其拼接成一个字符串id=1&name=tom
     *
     * */
    public static String sendHttpRequest(String httpUrl, Map<String, String> params) {

        try {
            URL url = new URL(httpUrl);//创建连接  http://localhost:8080/verif
            /*开启连接*/
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");//设置请求类型
            connection.setDoOutput(true);
            if (params != null && params.size() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                for (Map.Entry<String, String> entry : params.entrySet()) {

                    stringBuffer.append("&")
                            .append(entry.getKey())
                            .append("=")
                            .append(entry.getValue());
                }
                /*输出流*/
                connection.getOutputStream().write(stringBuffer.substring(1).toString().getBytes("UTF-8"));
            }
            /*发出请求*/
            connection.connect();
            /*获取返回信息输入流*/
            String response=StreamUtils.copyToString(connection.getInputStream(),Charset.forName("UTF-8"));//StreamUtils.copyToString()作用：将InputStream转换为字符串
            return  response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
