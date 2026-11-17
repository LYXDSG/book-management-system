package org.example.demo08.common;

import org.example.demo08.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration

public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    public WebMvcConfig (LoginInterceptor loginInterceptor){
        this.loginInterceptor=loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                //只管/api下的接口
                .excludePathPatterns("/api/auth/login","/api/auth/register");
                //登录接口本身当然不能要求登录
    }
    /*
    * 注意；放行GET(查询开放)的规则写在LoginInterceptor里，不在这里
    * 原因：路径匹配不认识HTTP方法，而/api/books恰恰是"GET放行，POST要登陆"
    * 另外：静态资源(/static下的html/js/css)不在/api/**范围里，天然不需要登录
    * */

    //CORS：前后端分离必备，前端5500访问后端8080需要它放行
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","DELETE","OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
    //Q：CORS介绍+系统学
}
