package com.marketplace.customer.service;

import org.springframework.stereotype.Service;

import com.marketplace.customer.dto.CustomerRequest;
import com.marketplace.customer.dto.CustomerResponse;
import com.marketplace.customer.mapper.CustomerMapper;
import com.marketplace.customer.repo.CustomerRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepo repo;
  private final CustomerMapper mapper;

  public void register(CustomerRequest request) {
    repo.saveAndFlush(mapper.toCustomer(request));
  }

  public CustomerResponse findByusername(String username) {
    return repo.findByUsername(username).map(mapper::toCustomerResponse)
        .orElse(new CustomerResponse(false, null, "", ""));
  }
}
