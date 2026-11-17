package org.example.demo08.mapper;

import org.apache.ibatis.annotations.*;
import org.example.demo08.entity.User;

@Mapper

public interface UserMapper {
    //Mapper类两个方法，1.按用户名查一个用户，2.插入一个新用户
    @Select("select * from t_user where username =#{username}")
    User findByName (@Param("username") String username);
    //找不到不是错误，返回Null是正常结果

    @Insert("insert into t_user (username,password) values(#{username},#{password}) ")
    @Options(useGeneratedKeys=true,keyProperty="id")
    //回填数据库的自增id到user对象
    int insert(User user);
    //int代表成功修改条数
}
