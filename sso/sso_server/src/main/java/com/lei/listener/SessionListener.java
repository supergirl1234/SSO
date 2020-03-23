package com.lei.listener;

import com.lei.db.MockDB;
import com.lei.util.HttpUtil;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Iterator;
import java.util.Set;

/*监听器*/
/*服务器端销毁session之后，要通知taobao和tmall这两个客户端销毁各自的session*/
@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        /*1、删除全局会话中的token*/
        /*2、删除数据库的用户信息*/
            /*获取token*/
        String token=(String) se.getSession().getServletContext().getAttribute("token");
           /*删除全局中的token*/
        se.getSession().getServletContext().removeAttribute("token");
           /*删除数据库中的token*/
        MockDB.tokenSet.remove(token);

        /*3、通知所有客户端销毁session,每个客户端都退出*/
        Set<String> set=MockDB.clientLogoutUrlMap.get(token);
        Iterator<String> iterator=set.iterator();
        while (iterator.hasNext()){
            HttpUtil.sendHttpRequest(iterator.next(),null);//与客户端进行连接通信，执行退出操作
        }
        /*将客户端的退出地址也从数据库中删除*/
        MockDB.clientLogoutUrlMap.remove(token);
    }
}
