package com.lei.controller;

import com.lei.util.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TmallHandler {
    @GetMapping("/tmall")
    public String index(Model model){

        model.addAttribute("serverLogoutUrl",SSOClientUtil.getServerLogoutUrl());//存放的退出页面的链接
        return "index";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        /*将session销毁*/
        session.invalidate();
        return "redirect:/tmall";
    }
}
