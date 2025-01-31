package com.furkanalniak.assignment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @GetMapping("/profile")
    public String getActiveProfile() {
        return "Active Profile: " + activeProfile;
    }
} 