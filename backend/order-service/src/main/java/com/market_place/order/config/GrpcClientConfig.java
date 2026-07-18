package com.market_place.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

import com.market_place.proto.inventory.InventoryServiceGrpc;
import com.market_place.proto.inventory.InventoryServiceGrpc.InventoryServiceBlockingStub;

@Configuration
public class GrpcClientConfig {

  @Bean

  InventoryServiceBlockingStub inventoryServiceBlockingStub(GrpcChannelFactory channelFactory) {

    return InventoryServiceGrpc.newBlockingStub(channelFactory.createChannel("inventory"));
  }
}
