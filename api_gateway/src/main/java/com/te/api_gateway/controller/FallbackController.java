package com.te.api_gateway.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/service1")
    public ResponseEntity<String> service1Fallback() {
        return ResponseEntity.status(HttpStatus.SC_SERVICE_UNAVAILABLE)
                .body("Service1 is currently unavailable. Please try again later.");
    }

    @GetMapping("/fallback/service2")
    public ResponseEntity<String> service2Fallback() {
        return ResponseEntity.status(HttpStatus.SC_SERVICE_UNAVAILABLE)
                .body("Service2 is currently unavailable. Please try again later.");
    }
}
