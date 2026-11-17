package org.example.demo08.service;

import org.example.demo08.dto.LoginDTO;
import org.example.demo08.dto.RegisterDTO;
import org.example.demo08.entity.User;

public interface UserService {
    User register(RegisterDTO dto);
    User login(LoginDTO dto);
}
