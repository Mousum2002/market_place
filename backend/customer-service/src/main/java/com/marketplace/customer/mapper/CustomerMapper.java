package com.marketplace.customer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.marketplace.customer.dto.CustomerContactDto;
import com.marketplace.customer.dto.CustomerRequest;
import com.marketplace.customer.dto.CustomerResponse;
import com.marketplace.customer.model.Customer;
import com.marketplace.proto.customer.CreateCustomerRequest;
import com.marketplace.proto.customer.CreateCustomerResponse;
import com.marketplace.proto.customer.CustomerAuthResponse;
import com.marketplace.proto.customer.CustomerContactResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

  @Mapping(target = "exists", constant = "true")
  @Mapping(target = "userId", source = "id")
  @Mapping(target = "passwordHash", source = "password")
  CustomerResponse toCustomerResponse(Customer customer);

  @Mapping(target = "exists", constant = "true")
  @Mapping(target = "userId", source = "id")
  CustomerContactDto toCustomerContactDto(Customer customer);

  @Mapping(target = "id", ignore = true)
  Customer toCustomer(CustomerRequest request);

  CustomerAuthResponse toCustomerAuthResponse(CustomerResponse response);

  CustomerContactResponse toCustomerContactResponse(CustomerContactDto response);

  CustomerResponse rpcRequestToCustomerResponse(CreateCustomerResponse request);

  @Mapping(target = "message", constant = "ok")
  CreateCustomerResponse customerRequestToRpcCreateRequest(CustomerResponse request);

  CustomerRequest rpcRequestoToRequest(CreateCustomerRequest request);
}
