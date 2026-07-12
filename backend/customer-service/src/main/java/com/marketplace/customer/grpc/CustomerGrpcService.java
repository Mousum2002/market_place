package com.marketplace.customer.grpc;

import org.springframework.grpc.server.service.GrpcService;

import com.marketplace.customer.mapper.CustomerMapper;
import com.marketplace.customer.service.CustomerService;
import com.marketplace.proto.customer.CreateCustomerRequest;
import com.marketplace.proto.customer.CreateCustomerResponse;
import com.marketplace.proto.customer.CustomerAuthResponse;
import com.marketplace.proto.customer.CustomerByUsernameRequest;
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
  public void createCustomer(CreateCustomerRequest request, StreamObserver<CreateCustomerResponse> responseObserver) {

    service.register(mapper.rpcRequestoToRequest(request));
    responseObserver.onNext(CreateCustomerResponse.newBuilder().setMessage("ok").build());
    responseObserver.onCompleted();
  }
}
