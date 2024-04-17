package com.tuancd.demo1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String image;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private Date createAt;
}
