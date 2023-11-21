package com.child.service.impl;

import com.child.entity.Child;
import com.child.repository.ChildRepository;
import com.child.service.mapper.ChildMapper;
import com.child.web.dto.ChildDTO;
import com.child.web.exception.AlreadyActiveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChildServiceImplTest {

    @Mock
    private ChildRepository childRepository;

    @Mock
    private ChildMapper childMapper;

    @InjectMocks
    private ChildServiceImpl childService;

    @Test
    public void whenAddChild_thenSavesChildSuccessfully() {
        ChildDTO childDTO = new ChildDTO(); // Initialize with valid data
        Child child = new Child(); // Corresponding entity
        when(childMapper.toEntity(any(ChildDTO.class))).thenReturn(child);
        when(childRepository.save(any(Child.class))).thenReturn(child);
        when(childMapper.toDTO(any(Child.class))).thenReturn(childDTO);

        ChildDTO savedChildDTO = childService.addChild(childDTO);

        assertNotNull(savedChildDTO);
        verify(childRepository).save(child);
    }

    @Test
    public void whenAddChild_thenGeneratesUniqueQRCode() {
        when(childRepository.existsByQrCode(anyString())).thenReturn(false);
        String qrCode = childService.getUniqueQRCode();
        assertNotNull(qrCode);
        assertFalse(qrCode.isEmpty());
    }

    @Test
    public void whenGetAllChildren_thenReturnsActiveChildren() {
        List<Child> children = Arrays.asList(new Child(), new Child());
        when(childRepository.findAllByIsActive(true)).thenReturn(children);

        List<ChildDTO> childDTOs = childService.getAllChildren();

        assertNotNull(childDTOs);
        assertEquals(children.size(), childDTOs.size());
    }

    @Test
    public void whenActivateChild_thenActivatesSuccessfully() {
        String qrCode = "validCode";
        Child child = new Child();
        ChildDTO childDTO = new ChildDTO(); // Assuming this is your DTO object

        when(childRepository.findByQrCode(qrCode)).thenReturn(Optional.of(child));
        when(childMapper.toDTO(any(Child.class))).thenReturn(childDTO); // Mock to return a DTO
        when(childRepository.save(any(Child.class))).thenReturn(child);

        ChildDTO activatedChildDTO = childService.activateChild(qrCode);

        assertNotNull(activatedChildDTO);
        assertTrue(child.isActive());
    }

    @Test
    public void whenActivateNonExistingChild_thenThrowsException() {
        String qrCode = "invalidCode";
        when(childRepository.findByQrCode(qrCode)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> childService.activateChild(qrCode));
    }

    @Test
    public void whenActivateAlreadyActiveChild_thenThrowsAlreadyActiveException() {
        String qrCode = "alreadyActiveCode";
        Child child = new Child();
        child.setActive(true);
        when(childRepository.findByQrCode(qrCode)).thenReturn(Optional.of(child));

        assertThrows(AlreadyActiveException.class, () -> childService.activateChild(qrCode));
    }
}
