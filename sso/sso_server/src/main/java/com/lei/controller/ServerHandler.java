package com.lei.controller;

import com.lei.db.MockDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
@Slf4j
public class ServerHandler {

    /*跳转到服务器端认证中心了*/
    @RequestMapping("/checkToken")
    public String checkToken(String redirectUrl, HttpSession session, Model model, HttpServletRequest request) {

        /*考虑到两种情况：1、是否有token；2、是否登录*/
        /*获取全局token*/
        String token = (String) session.getServletContext().getAttribute("token");
        if (StringUtils.isEmpty(token)) {//如果token是空的,说明是第一次登录
            /*则需要去登录*/
            model.addAttribute("redirectUrl", redirectUrl);//存储回调的那个地址，在这里即是：返回登录淘宝的地址
            return "login";
        } else {
            /*如果token不空，则需要验证token*/
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getValue().equals(token)) {
                    /*验证通过，返回客户端*/
                    log.info("token验证通过");
                    return "redirect:" + redirectUrl + "?token=" + token;
                }
            }
        }
        /*如果上面两步都没成功，则重新登录*/
        model.addAttribute("redirectUrl", redirectUrl);//存储回调的那个地址，在这里即是：返回登录淘宝的地址
        return "login";
    }


    /*登录*/
    @GetMapping("/login")
    public String login(String username, String password, String redirectUrl, HttpSession session, Model model) {

        /*判断登录*/
        /*如果登录成功*/
        if ("admin".equals(username) && "123123".equals(password)) {
            /*1、登录成功，则创建token*/
            String token = UUID.randomUUID().toString();
            /*打印日志*/
            log.info("token创建成功，token=" + token);
            /*2、存储全局Session*/
            session.getServletContext().setAttribute("token", token);
            /*3、token的信息还要保存到数据库中*/
            MockDB.tokenSet.add(token);
            /*4、返回客户端*/
            return "redirect:" + redirectUrl + "?token=" + token;//采用重定向的方式返回淘宝
        } else {
            /*如果没有登录成功*/
            /*则需要重新去登录*/
            log.error("用户名密码错误! username={},password={}", username, password);
            model.addAttribute("redirectUrl", redirectUrl);//存储回调的那个地址，在这里即是：返回登录淘宝的地址
            return "login";
        }

    }

    @RequestMapping("/verify")
    @ResponseBody
    /*客户端连接服务器端，发送信息的时候，就带过来token和clientLogoutUrl了。这边可以直接写在参数里面*/
    public String verifyToken(String token,String clientLogoutUrl) {
        if (MockDB.tokenSet.contains(token)) {

            /*将客户端的退出地址传到服务器端，并将其保存到数据库中*/
            Set<String> set = MockDB.clientLogoutUrlMap.get(token);
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(clientLogoutUrl);
            MockDB.clientLogoutUrlMap.put(token,set);
            return "true";
        }
        return"false";


    }


    /*服务器端销毁session*/
    @RequestMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();//服务器端销毁session
        return "login";
    }
}
