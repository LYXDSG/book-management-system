package org.example.demo08.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterDTO {
    @NotBlank(message="用户名不能为空，不能为空格")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
            message = "密码必须至少6位，且同时包含字母和数字")
    //正则表达式确定密码格式
    @NotBlank(message="密码不能为空，不能为空格")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
