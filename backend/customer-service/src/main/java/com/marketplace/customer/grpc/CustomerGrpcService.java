package com.marketplace.customer.grpc;

import java.util.UUID;

import org.springframework.grpc.server.service.GrpcService;

import com.marketplace.customer.dto.CustomerContactDto;
import com.marketplace.customer.mapper.CustomerMapper;
import com.marketplace.customer.service.CustomerService;
import com.marketplace.proto.customer.CreateCustomerRequest;
import com.marketplace.proto.customer.CreateCustomerResponse;
import com.marketplace.proto.customer.CustomerAuthResponse;
import com.marketplace.proto.customer.CustomerByIdRequest;
import com.marketplace.proto.customer.CustomerByUsernameRequest;
import com.marketplace.proto.customer.CustomerContactResponse;
import com.marketplace.proto.customer.CustomerServiceGrpc.CustomerServiceImplBase;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@GrpcService
@RequiredArgsConstructor
public class CustomerGrpcService extends CustomerServiceImplBase {

  private final CustomerService service;
  private final CustomerMapper mapper;

  @Override
  public void findCustomerByUsername(CustomerByUsernameRequest request,
      StreamObserver<CustomerAuthResponse> responseObserver) {
    responseObserver.onNext(mapper.toCustomerAuthResponse(service.findByusername(request.getUsername())));
    responseObserver.onCompleted();
  }

  @Override
  public void findCustomerById(CustomerByIdRequest request, StreamObserver<CustomerContactResponse> responseObserver) {
    CustomerContactDto response;
    try {
      response = service.findContactById(UUID.fromString(request.getCustomerId()));
    } catch (IllegalArgumentException e) {
      response = new CustomerContactDto(false, null, "", "");
    }
    responseObserver.onNext(mapper.toCustomerContactResponse(response));
    responseObserver.onCompleted();
  }

  @Override
  public void createCustomer(CreateCustomerRequest request, StreamObserver<CreateCustomerResponse> responseObserver) {
    responseObserver.onNext(service.register(mapper.rpcRequestoToRequest(request)));
    responseObserver.onCompleted();
  }
}
