package com.lei.interceptor;

import com.lei.util.HttpUtil;
import com.lei.util.SSOClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/*拦截器，需要继承HandlerInterceptor接口*/
@Slf4j
public class TmallInterceptor implements HandlerInterceptor {

    /*当访问淘宝页面的请求过来的时候，进行拦截，所以在之前拦截*/

    /*
     * true：放行
     * false：不放行
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*1、判断是否登录taobao了*/

        HttpSession session=request.getSession();//获取Session
        Boolean isLogin= (Boolean) session.getAttribute("isLogin");
        /*如果isLogin存在，则说明已经登录该页面了；如果不存在，说明没有登录该页面*/
        if(isLogin!=null&&isLogin){
            return  true;
        }
        /*2、如果没有登录淘宝，则判断是否已经有token了，因为可能没有登录taobao，但是已经登录tmall了*/
        String token=request.getParameter("token");
        /*如果有token，则验证token*/
        if(!StringUtils.isEmpty(token)){

            //验证token
            log.info("token存在，需要验证");
            /*向服务器端发起验证*/
            String httpUrl=SSOClientUtil.SERVER_HOST_URL+"/verify";
            HashMap<String,String> params=new HashMap<>();
            params.put("token",token);
            params.put("clientLogoutUrl",SSOClientUtil.getClientLogoutUrl());//保存客户端的退出地址
            String isVefify=HttpUtil.sendHttpRequest(httpUrl,params);
            if(("true").equals(isVefify)){
                log.info("token验证通过，token={}",token)
                /*token保存到本地,存到浏览器中国*/;
                Cookie cookie=new Cookie("token",token);
                response.addCookie(cookie);
                /*也登录成功了，则设置isLogin*/
                session.setAttribute("isLogin",true);
                return  true;
            }
        }

        /*3、如果也没有token，则跳转到认证中心，进行登录淘宝*/
        SSOClientUtil.redirectToCheckToken(request,response);
        return  false;

    }
}
