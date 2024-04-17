package com.tuancd.demo1.dto;

import com.tuancd.demo1.model.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRequest {
    @NotEmpty(message = "name is required")
    private String username;
    @NotEmpty(message = "email is required")
    private String email;
    @NotNull
    private String password;
    private MultipartFile image;
    private Role role;
}
