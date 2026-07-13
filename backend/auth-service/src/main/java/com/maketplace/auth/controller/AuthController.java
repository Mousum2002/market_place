package com.maketplace.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maketplace.auth.dto.AuthResponse;
import com.maketplace.auth.dto.LoginRequest;
import com.maketplace.auth.dto.RegisterRequest;
import com.maketplace.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService service;

  @PostMapping("/register")
  public ResponseEntity<String> createCustomer(@RequestBody RegisterRequest request) {
    log.info(request.username());
    return ResponseEntity.ok(service.register(request).getMessage());

  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(service.login(request));
  }
}
