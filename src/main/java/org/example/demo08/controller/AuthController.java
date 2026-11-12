package org.example.demo08.controller;

import jakarta.validation.Valid;
import org.example.demo08.common.Result;
import org.example.demo08.common.TokenStore;
import org.example.demo08.dto.LoginDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//验证登录
public class AuthController {
    private final TokenStore tokenStore;
    public AuthController(TokenStore tokenStore){
        this.tokenStore=tokenStore;
    }

    //登录：POST/api/auth/login{"username":"admin","password":"123456"}
    //返回：{"code":200,"msg":"登录成功","data":"b3i1u3bo...token..."}

    //教学项目用固定账号admin/123456:真实项目必须：
    //(1)查user表       (2) 密码用BCrypt加密后对比（数据库绝不能存明文密码）
    //(3)限制登录失败次数，防止暴力破解
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid LoginDTO dto){
        if(!"admin".equals(dto.getUsername())||!"123456".equals(dto.getPassword())){
            return Result.fail(401,"用户名或密码错误");
        }
        String token= tokenStore.create(dto.getUsername());
        return Result.ok("登录成功",token);
    }
    //注销，带上当前token调一次，服务器就把它作废
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("token") String token){
        tokenStore.remove(token);
        return Result.ok("已退出登录",null);
    }

}
