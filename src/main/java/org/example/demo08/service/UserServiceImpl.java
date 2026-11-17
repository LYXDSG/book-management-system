package org.example.demo08.service;

import org.example.demo08.common.BusinessException;
import org.example.demo08.dto.LoginDTO;
import org.example.demo08.dto.RegisterDTO;
import org.example.demo08.entity.User;
import org.example.demo08.mapper.UserMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service

public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    //换成BCrypt
    public UserServiceImpl(UserMapper userMapper,PasswordEncoder passwordEncoder){
        this.userMapper=userMapper;
        this.passwordEncoder=passwordEncoder;
    }
    //Mapper有两个方法，一个是根据用户名查找用户，一个是插入新用户
    @Override
    public User register(RegisterDTO dto){
        //1.取用户名去空格
        String safeName=dto.getUsername().trim();
        //2.查重,返回null或者非null分别怎么处理
//        User user=userMapper.findByName(safeName);
//        if(user!=null) throw new BusinessException(400,"用户名已存在");
/**
 * 这种写法会报NPE，因为过了if的user都是null，无法接收，这里不应该接收
 * userMapper的值，直接判断就行，再新建一个user对象接收数据
 */
        if(userMapper.findByName(safeName)!=null) throw new BusinessException(400,"用户名已存在");
        User user=new User();
        //3.密码哈希，什么是哈希？为什么要哈希？
        //哈希就是"绞肉机"，能把密码变成唯一的哈希码，但无法再转回密码，就像绞肉一样不可逆（属于特性）。
        //哈希的目的是为了避免明文存储密码
//        String hash = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        //换成BCrypt
        String hash = passwordEncoder.encode(dto.getPassword());
        user.setPassword(hash);
        //4.组装User
        user.setUsername(safeName);
        //5.插入一个新用户
        try {
            userMapper.insert(user);
            //此时User的id已经有值了
        }catch(DuplicateKeyException e){
            throw new BusinessException(400,"用户名已存在");
        }
        //捕获并发插入时重名的异常

        return user;

    }

    @Override
    public User login(LoginDTO dto){
        String safeName=dto.getUsername().trim();
        User user =userMapper.findByName(safeName);
        if(user==null)throw new BusinessException(400,"用户名或密码错误");
        //在数据库里找到相同的用户名再开始比对密码的哈希值
//        String hash = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        //换成BCrypt
//        String hash = passwordEncoder.encode(dto.getPassword());
        //改成BCrypt就不能这样存了，因为每次盐不同生成的码也不一样，所以.equals()永远为假。
        //输入密码的哈希值
//        if(hash!=user.getPassword()) throw new BusinessException(400,"密码错误");
// ==，!=比的是引用不是内容，hash和user.getPassword()都是单独的String所以!=永远为真
//        if (!hash.equals(user.getPassword())) throw new BusinessException(400,"用户名或密码错误");
        if(!passwordEncoder.matches(dto.getPassword(),user.getPassword())) throw new BusinessException(400,"用户名或密码错误");
        //用.equals()替代。这个比的才是内容
        //两关都过了返回user
        return user;
    }

}
