package org.example.demo08.common;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Service

public class TokenStore {
    private final Map<String,String> tokens=new ConcurrentHashMap<>();

    //登录成功时调用，生成一个随机token，记住它属于哪个用户
    public String create (String username){
        //UUID生成本机不重复字符串，去掉横线短一点
        String token = UUID.randomUUID().toString().replace("-","");
        tokens.put(token,username);
        return token;
    }

    //拦截器调用，token有效就返回用户名，无效过期就返回null
    public String getUsername(String token){
        if(token==null||token.isBlank()) return null;
        return tokens.get(token);
        //map里get(key)取value值
    }

    //注销时调用，删掉这个token
    public void remove (String token ){
        tokens.remove(token);
    }
}
