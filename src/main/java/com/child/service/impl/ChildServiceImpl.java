package com.child.service.impl;

import com.child.entity.Child;
import com.child.repository.ChildRepository;
import com.child.service.IChildService;
import com.child.service.mapper.ChildMapper;
import com.child.utils.StringUtil;
import com.child.web.dto.ChildDTO;
import com.child.web.exception.AlreadyActiveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChildServiceImpl implements IChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ChildMapper childMapper;

    @Override
    public ChildDTO addChild(ChildDTO childDTO) {
        log.info("Adding a new child: {}", childDTO);
        String qrCode = getUniqueQRCode();
        childDTO.setQrCode(qrCode);
        Child child = childMapper.toEntity(childDTO);
        Child savedChild = childRepository.save(child);
        return childMapper.toDTO(savedChild);
    }

    public String getUniqueQRCode() {
        String qrCode;
        boolean isUnique;
        do {
            qrCode = StringUtil.generateUniqueQRCode();
            isUnique = !childRepository.existsByQrCode(qrCode);
        } while (!isUnique);
        return qrCode;
    }

    @Override
    public List<ChildDTO> getAllChildren() {
        log.info("Retrieving all children");
        return childRepository.findAllByIsActive(true).stream()
                .map(childMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ChildDTO activateChild(String qrCode) {
        log.info("Activating child with QR code: {}", qrCode);
        Child child = childRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new IllegalArgumentException("Child with QR code not found"));

        if (child.isActive()) {
            throw new AlreadyActiveException("Child with QR code " + qrCode + " is already active.");
        }

        child.setActive(true);
        Child updatedChild = childRepository.save(child);
        return childMapper.toDTO(updatedChild);
    }
}