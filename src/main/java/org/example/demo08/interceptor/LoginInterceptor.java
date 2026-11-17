package org.example.demo08.interceptor;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo08.common.TokenStore;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
//拦截器属于SpringMVC管理，在controller之前执行，
@Service

public class LoginInterceptor implements HandlerInterceptor {

    //请求头里携带token的字段名，前后端约定好
    public static final String TOKEN_HEADER="token";

    //登录后把用户名放进request属性，后面的Controller/Service想用就取
    public static final String LOGIN_USER="loginuser";
    //Q;这两步是啥意思，这两条数据是干啥的

    private final TokenStore tokenStore;
    public LoginInterceptor(TokenStore tokenStore){
        this.tokenStore=tokenStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response,Object handler) throws Exception{
        //(1)跨域预检请求(浏览器发OPTIONS探路)直接放行，否则前端会被拦在门外
        if(HttpMethod.OPTIONS.matches(request.getMethod())) return true;

        //(2)查询开放：GET是只读语义，放行
        if(HttpMethod.GET.matches(request.getMethod())) return true;

        //(3)其余方法：(POST/PUT/DELETE)必须带有效token
        String token =request.getHeader(TOKEN_HEADER);
        String username=tokenStore.getUsername(token);

        if(username==null){
            /*
            * 关键细节：这里不能抛异常
            * 拦截器运行在DispatcherServlet调HandlerExceptionResolver之前，
            * preHandle里抛异常不会走@RestControllerAdvice的全局处理
            * 结果就是前端收到一坨错误页，所以这里直接把401的JSON写回响应
            * */
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //HTTP 401状态行
            response.setContentType("application/json;charset=UTF-8");
            //响应头
            response.getWriter().write(
                    "{\"code\":401,\"msg\":\"未登录或登录状态已过期，请先调用/api/auth/login获取token\",\"data\":null}"
            );
            return false;
            //中断，请求不会到达Controller
        }
        //(4)认出用户了，存进request，后面需要"当前登陆人"的地方直接取
        request.setAttribute(LOGIN_USER,username);
        return true;
        //放行
    }


}
