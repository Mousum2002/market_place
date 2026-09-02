package com.marketplace.customer.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.customer.dto.CustomerContactDto;
import com.marketplace.customer.dto.CustomerCreatedLogEvent;
import com.marketplace.customer.dto.CustomerRequest;
import com.marketplace.customer.dto.CustomerResponse;
import com.marketplace.customer.mapper.CustomerMapper;
import com.marketplace.customer.model.Customer;
import com.marketplace.customer.repo.CustomerRepo;
import com.marketplace.proto.customer.CreateCustomerResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepo repo;
  private final CustomerMapper mapper;
  private final KafkaTemplate<String, byte[]> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Value("${app.kafka.topics.customer-created}")
  String customerCreatedTopic;

  public CreateCustomerResponse register(CustomerRequest request) {
    Customer customer = repo.saveAndFlush(mapper.toCustomer(request));
    kafkaTemplate.send(customerCreatedTopic, customer.getId().toString(),
        toJsonBytes(new CustomerCreatedLogEvent(customer.getId(), Instant.now())));

    return CreateCustomerResponse.newBuilder().setMessage("Customer Created Succesfully").build();
  }

  public CustomerResponse findByusername(String username) {
    return repo.findByUsername(username).map(mapper::toCustomerResponse)
        .orElse(new CustomerResponse(false, null, "", ""));
  }

  public CustomerContactDto findContactById(UUID customerId) {
    return repo.findById(customerId).map(mapper::toCustomerContactDto)
        .orElse(new CustomerContactDto(false, null, "", ""));
  }

  private byte[] toJsonBytes(CustomerCreatedLogEvent event) {
    try {
      return objectMapper.writeValueAsBytes(event);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Failed to serialize CustomerCreatedLogEvent", e);
    }
  }
}
