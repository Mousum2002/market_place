package com.marketplace.customer.config;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;

import io.grpc.Status;

@Configuration
public class GrpcExceptionConfig {

  @Bean
  GrpcExceptionHandler customerGrpcExceptionHandler() {
    return throwable -> {
      if (throwable instanceof DataIntegrityViolationException ex) {
        String constraint = extractConstraintName(ex);

        return switch (constraint) {
          case "customers_username" -> Status.ALREADY_EXISTS
              .withDescription("Username already exists.")
              .asException();

          case "customers_email" -> Status.ALREADY_EXISTS
              .withDescription("Email already exists.")
              .asException();

          default -> Status.INTERNAL
              .withDescription("Database error.")
              .asException();
        };
      }

      return Status.INTERNAL
          .withDescription("Unexpected server error.")
          .asException();
    };
  }

  private String extractConstraintName(DataIntegrityViolationException ex) {
    Throwable cause = ex.getCause();

    if (cause instanceof ConstraintViolationException cve) {
      return cve.getConstraintName() != null
          ? cve.getConstraintName()
          : "UNKNOWN_CONSTRAINT";
    }

    Throwable root = ex.getRootCause();
    if (root instanceof ConstraintViolationException cve) {
      return cve.getConstraintName() != null
          ? cve.getConstraintName()
          : "UNKNOWN_CONSTRAINT";
    }

    return "UNKNOWN_CONSTRAINT";
  }
}
