package org.example.demo08.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message="用户名不能为空，不能为空格")
    //@NotNull只拦Null，@NotEmpty拦Null+空串，
    //@NotBlank拦Null+空串+空格
    private String username;
    @NotBlank(message="密码不能为空，不能为空格")
    private String password;

    //getter和setter方法

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
