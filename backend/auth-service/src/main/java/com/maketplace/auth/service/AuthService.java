package com.maketplace.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.maketplace.auth.dto.AuthResponse;
import com.maketplace.auth.dto.LoginRequest;
import com.maketplace.auth.dto.RegisterRequest;
import com.marketplace.proto.customer.CreateCustomerRequest;
import com.marketplace.proto.customer.CreateCustomerResponse;
import com.marketplace.proto.customer.CustomerAuthResponse;
import com.marketplace.proto.customer.CustomerByUsernameRequest;
import com.marketplace.proto.customer.CustomerServiceGrpc.CustomerServiceBlockingStub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final CustomerServiceBlockingStub stub;
  private final PasswordEncoder encoder;
  private final JwtService jwtService;

  public AuthResponse login(LoginRequest request) {
    CustomerAuthResponse customer;

    customer = stub.findCustomerByUsername(CustomerByUsernameRequest.newBuilder()
        .setUsername(request.username())
        .build());
    if (!customer.getExists()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    if (!encoder.matches(request.password(), customer.getPasswordHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    String token = jwtService.generateToken(customer.getUserId(), customer.getUsername());
    return new AuthResponse(token);
  }

  public CreateCustomerResponse register(RegisterRequest request) {
    return stub.createCustomer(CreateCustomerRequest.newBuilder()
        .setUsername(request.username())
        .setEmail(request.email())
        .setPassword(encoder.encode(request.password()))
        .build());
  }
}
