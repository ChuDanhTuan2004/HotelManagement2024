package com.tuancd.demo1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
    public BookingRequest(LocalDate checkInDate, LocalDate checkOutDate, String guestName, int numberOfAdults, int numberOfChildren) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestName = guestName;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public BookingRequest() {
    }

    @NotNull(message = "check in date is required")
    private LocalDate checkInDate;
    @NotNull(message = "check out date is required")
    private LocalDate checkOutDate;
    @NotEmpty(message = "guest name is required")
    private String guestName;
    @Min(value = 0, message = "min number of adults is 0")
    private int numberOfAdults;
    @Min(value = 0, message = "min number of children is 0")
    private int numberOfChildren;
    private String confirmCode;
}
