package com.child.service.mapper;

import com.child.entity.Child;
import com.child.web.dto.ChildDTO;
import org.springframework.stereotype.Component;

@Component
public class ChildMapper {
    public Child toEntity(ChildDTO dto) {
        if (dto == null) {
            return null;
        }
        Child child = new Child();
        child.setName(dto.getName());
        child.setAge(dto.getAge());
        child.setGender(dto.getGender());
        child.setQrCode(dto.getQrCode());
        child.setActive(false);
        return child;
    }

    public ChildDTO toDTO(Child child) {
        if (child == null) {
            return null;
        }

        ChildDTO dto = new ChildDTO();
        dto.setChildId(child.getChildId());
        dto.setName(child.getName());
        dto.setAge(child.getAge());
        dto.setGender(child.getGender());
        dto.setQrCode(child.getQrCode());
        dto.setActive(child.isActive());
        return dto;
    }
}
