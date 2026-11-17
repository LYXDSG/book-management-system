package org.example.demo08.controller;

import jakarta.validation.Valid;
import org.example.demo08.common.BusinessException;
import org.example.demo08.common.Result;
import org.example.demo08.common.TokenStore;
import org.example.demo08.dto.LoginDTO;
import org.example.demo08.dto.RegisterDTO;
import org.example.demo08.entity.User;
import org.example.demo08.mapper.UserMapper;
import org.example.demo08.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//验证登录
public class AuthController {
    private final TokenStore tokenStore;
    private final UserService userService;
//    private final UserMapper userMapper;
    //Mapper不应该出现在Controller，业务代码也不该出现在Controller，所以提取到实现类
    public AuthController (TokenStore tokenStore,UserService userService){
        this.userService=userService;
        this.tokenStore=tokenStore;
//        this.userMapper=userMapper;
    }
    //构造器注入多个bean只能在同一个构造器里。
    //Spring 只会用一个构造器。所以多个依赖要写成同一个构造器的多个参数。

    //登录：POST/api/auth/login{"username":"admin","password":"123456"}
    //返回：{"code":200,"msg":"登录成功","data":"b3i1u3bo...token..."}

    //教学项目用固定账号admin/123456:真实项目必须：
    //(1)查user表       (2) 密码用BCrypt加密后对比（数据库绝不能存明文密码）
    //(3)限制登录失败次数，防止暴力破解
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid LoginDTO dto){
//        if(!"admin".equals(dto.getUsername())||!"123456".equals(dto.getPassword())){
//            return Result.fail(401,"用户名或密码错误");
//        }
//        String token= tokenStore.create(dto.getUsername());
//        return Result.ok("登录成功",token);

//*业务解耦
//        String safeName=dto.getUsername().trim();
//        User user =userMapper.findByName(safeName);
//        if(user==null)throw new BusinessException(400,"用户名不存在");
//       //在数据库里找到相同的用户名再开始比对密码的哈希值
//        String hash = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes());
//        //输入密码的哈希值
////        if(hash!=user.getPassword()) throw new BusinessException(400,"密码错误");
//// ==，!=比的是引用不是内容，hash和user.getPassword()都是单独的String所以!=永远为真
//        if (!hash.equals(user.getPassword())) throw new BusinessException(400,"密码错误");
//        //用.equals()替代。这个比的才是内容
//        //两关都过了再发token
// *业务内容
        User user=userService.login(dto);
        String token = tokenStore.create(user.getUsername());
        //这里dto.getUsername()和user.getUsername()都可以吗
        //用user.getUsername(),这是经过检验后的值
        return Result.ok("登录成功",token);

    }
    //注销，带上当前token调一次，服务器就把它作废
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("token") String token){
        tokenStore.remove(token);
        return Result.ok("已退出登录",null);
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody @Valid RegisterDTO dto){
//        tokenStore.create(userService.register(dto).getUsername());
//        return Result.ok("已注册成功",userService.register(dto).getPassword());
        //register不能调用两次否则会"假失败"
        User user =userService.register(dto);
        String token=tokenStore.create(user.getUsername());
        return Result.ok("已注册成功",token);
    }

}
