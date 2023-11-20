package com.child.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/health")
public class HealthController {

    @GetMapping
    public String health() {
        return "I am running fine";
    }
}
