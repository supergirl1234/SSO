package com.lei.controller;

import com.lei.util.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/*
 * 访问淘宝页面
 * */
@Controller
public class TaoBaoHandler {

    @GetMapping("/taobao")
    public String index(Model model) {
        model.addAttribute("serverLogoutUrl", SSOClientUtil.getServerLogoutUrl());
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        /*将session销毁*/
        session.invalidate();
        return "redirect:/taobao";
    }

}
