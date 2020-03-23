package com.lei.config;

import com.lei.listener.SessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*配置监听器*/
@Configuration
public class ListenerConfig  implements WebMvcConfigurer {

    /*自己定义一个bean，进行注入*/
    @Bean /*自动注入到IOC中*/
    public ServletListenerRegistrationBean bean(){

        ServletListenerRegistrationBean bean=new ServletListenerRegistrationBean();
        bean.setListener(new SessionListener());
        return  bean;
    }
}
