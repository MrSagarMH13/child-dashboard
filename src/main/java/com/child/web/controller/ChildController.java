package com.child.web.controller;

import com.child.service.IChildService;
import com.child.web.dto.ChildDTO;
import com.child.web.response.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/child")
public class ChildController {

    private final IChildService childService;

    public ChildController(IChildService childService) {
        this.childService = childService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> addChild(@RequestBody ChildDTO childDTO) {
        log.info("Request to add child: {}", childDTO);
        ChildDTO createdChild = childService.addChild(childDTO);
        SuccessResponse response = new SuccessResponse("Child added successfully", createdChild);
        log.info("Child added successfully: {}", createdChild);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> getAllChildren() {
        log.info("Request to get all children");
        List<ChildDTO> children = childService.getAllChildren();
        SuccessResponse response = new SuccessResponse("Children list fetched successfully", children);
        log.info("Children list fetched successfully, count: {}", children.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/activate")
    public ResponseEntity<SuccessResponse> activateChild(@RequestParam("qrCode") String qrCode) {
        log.info("Request to activate child with QR code: {}", qrCode);
        ChildDTO activatedChild = childService.activateChild(qrCode);
        SuccessResponse response = new SuccessResponse("Child activated successfully", activatedChild);
        log.info("Child activated successfully: {}", activatedChild);
        return ResponseEntity.ok(response);
    }

}
