package com.tuancd.demo1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RoomRequest {
    @NotEmpty(message = "name is required")
    private String name;
    private Long roomType;
    @Min(0)
    private double price;
    private MultipartFile imageFile;
    @Size(min = 2, message = "The description should be at least 2 characters")
    @Size(max = 2000, message = "The description cannot exceed 2000 characters")
    private String description;
}