package com.maketplace.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

import com.marketplace.proto.customer.CustomerServiceGrpc;
import com.marketplace.proto.customer.CustomerServiceGrpc.CustomerServiceBlockingStub;

@Configuration
public class GrpcClientConfig {

  @Bean
  CustomerServiceBlockingStub customerServiceBlockingStub(GrpcChannelFactory channels) {
    return CustomerServiceGrpc.newBlockingStub(channels.createChannel("customer"));
  }
}
