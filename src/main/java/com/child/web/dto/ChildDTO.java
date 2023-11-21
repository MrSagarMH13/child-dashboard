package com.child.web.dto;

import jakarta.annotation.Nonnull;
import lombok.Data;

@Data
public class ChildDTO {
    private Long childId;
    private String name;
    private int age;
    private String gender;
    private String qrCode;
    private boolean isActive;
}
