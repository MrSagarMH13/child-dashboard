package com.child.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long childId;
    private String name;
    private int age;
    private String gender;
    @Column(updatable = false, unique = true, nullable = false)
    private String qrCode;
    private boolean isActive;
}
