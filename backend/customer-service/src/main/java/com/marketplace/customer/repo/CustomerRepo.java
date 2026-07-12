package com.marketplace.customer.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.customer.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, UUID> {

  Optional<Customer> findByUsername(String username);
}
