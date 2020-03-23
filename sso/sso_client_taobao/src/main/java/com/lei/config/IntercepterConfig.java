package com.lei.config;

import com.lei.interceptor.TaobaoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*配置拦截器*/
@Configuration
public class IntercepterConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*定义拦截路径*/
        String[] patterns={"/taobao"};
        registry.addInterceptor(new TaobaoInterceptor()).addPathPatterns(patterns);
    }
}
