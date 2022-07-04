package com.test.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @GetMapping(path = "/ready")
    public ResponseEntity isReady() {
        return new ResponseEntity(OK);
    }

}