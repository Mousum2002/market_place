package com.market_place.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

import com.market_place.proto.inventory.InventoryServiceGrpc;
import com.market_place.proto.inventory.InventoryServiceGrpc.InventoryServiceBlockingStub;
import com.marketplace.proto.product.ProductServiceGrpc;
import com.marketplace.proto.product.ProductServiceGrpc.ProductServiceBlockingStub;

@Configuration
public class GrpcClientConfig {

  @Bean
  public InventoryServiceBlockingStub inventoryServiceBlockingStub(GrpcChannelFactory channelFactory) {
    return InventoryServiceGrpc.newBlockingStub(channelFactory.createChannel("static://localhost:9091"));
  }

  @Bean
  public ProductServiceBlockingStub productServiceBlockingStub(GrpcChannelFactory channelFactory) {
    return ProductServiceGrpc.newBlockingStub(channelFactory.createChannel("static://localhost:9092"));
  }
}
