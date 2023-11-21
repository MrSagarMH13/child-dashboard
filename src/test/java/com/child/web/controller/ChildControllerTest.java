package com.child.web.controller;

import com.child.service.IChildService;
import com.child.web.dto.ChildDTO;
import com.child.web.exception.AlreadyActiveException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChildController.class)
@AutoConfigureMockMvc
public class ChildControllerTest {

    @MockBean
    private IChildService childService;

    @Autowired
    private MockMvc mockMvc;

    // Test cases
    @Test
    public void whenAddValidChild_thenReturnsSuccess() throws Exception {
        ChildDTO childDTO = new ChildDTO(); // Initialize with valid data
        given(childService.addChild(any(ChildDTO.class))).willReturn(childDTO);

        mockMvc.perform(post("/v1/api/child")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(childDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Child added successfully"));
    }

    @Test
    public void whenGetChildrenAndNonEmpty_thenReturnsListOfChildren() throws Exception {
        List<ChildDTO> children = Arrays.asList(new ChildDTO(), new ChildDTO());
        given(childService.getAllChildren()).willReturn(children);

        mockMvc.perform(get("/v1/api/child"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    public void whenGetChildrenAndEmpty_thenReturnsEmptyList() throws Exception {
        given(childService.getAllChildren()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/api/child"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    public void whenActivateExistingChild_thenReturnsSuccess() throws Exception {
        ChildDTO childDTO = new ChildDTO(); // Assume it represents an activated child
        given(childService.activateChild(anyString())).willReturn(childDTO);

        mockMvc.perform(put("/v1/api/child/activate?qrCode=validCode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Child activated successfully"));
    }

    @Test
    public void whenActivateAlreadyActivatedChild_thenReturnsBadRequest() throws Exception {
        String qrCode = "invalidCode";
        given(childService.activateChild(qrCode)).willThrow(new AlreadyActiveException("Child with QR code " + qrCode + " is already active."));

        mockMvc.perform(put("/v1/api/child/activate?qrCode=" + qrCode))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Child with QR code " + qrCode + " is already active."));
    }

    // Helper method to convert object to JSON string
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
