package com.tuancd.demo1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "booked_rooms")
public class BookedRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestName;
    private int numberOfAdults;
    private int numberOfChildren;
    private int totalNumberOfGuest;
    private String confirmCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void calculateTotalNumberOfGuest() {
        this.totalNumberOfGuest = this.numberOfAdults + numberOfChildren;
    }

    public void setNumOfAdults(int numOfAdults) {
        numberOfAdults = numOfAdults;
        calculateTotalNumberOfGuest();
    }

    public void setNumOfChildren(int numOfChildren) {
        numberOfChildren = numOfChildren;
        calculateTotalNumberOfGuest();
    }
}
