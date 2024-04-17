package com.tuancd.demo1.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "room_types")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
