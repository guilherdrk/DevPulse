package com.guilherdrk.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.guilherdrk.backend.dto.PingResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(value = "/api/v1")

class PingController {
    @GetMapping(value = "/ping")
    public ResponseEntity<PingResponse> ping() {
        return ResponseEntity.ok(PingResponse.created());
    }
}