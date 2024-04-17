package com.tuancd.demo1.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDTO {
    @NotEmpty(message = "name is required")
    private String username;
    @NotEmpty(message = "email is required")
    private String email;
    private String password;
    private MultipartFile image;
    @NotEmpty(message = "role is required")
    private Long role;
}
