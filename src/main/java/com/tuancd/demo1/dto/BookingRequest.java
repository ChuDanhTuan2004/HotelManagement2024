package com.tuancd.demo1.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class BookingRequest {
    @NotNull(message = "check in date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkInDate;
    @NotNull(message = "check out date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkOutDate;
    @NotEmpty(message = "guest name is required")
    private String guestName;
    @Min(value = 1, message = "min number of adults is 1")
    @Max(value = 4, message = "max number of adults is 4")
    private int numberOfAdults;
    @Min(value = 0, message = "min number of children is 0")
    @Max(value = 4, message = "max number of children is 4")
    private int numberOfChildren;
    private String confirmCode;
}
