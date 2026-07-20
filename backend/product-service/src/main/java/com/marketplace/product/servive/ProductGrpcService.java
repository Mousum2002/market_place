package com.marketplace.product.servive;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.grpc.server.service.GrpcService;

import com.marketplace.proto.product.ProductPriceItem;
import com.marketplace.proto.product.ProductPriceRequest;
import com.marketplace.proto.product.ProductPriceResponse;
import com.marketplace.proto.product.ProductServiceGrpc.ProductServiceImplBase;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@GrpcService
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceImplBase {

  private final ProductService productService;

  @Override
  public void getPrices(ProductPriceRequest request, StreamObserver<ProductPriceResponse> responseObserver) {

    Map<String, BigDecimal> prices = productService.getPricesByIds(request.getProductIdsList());

    ProductPriceResponse.Builder response = ProductPriceResponse.newBuilder();

    prices.forEach((productId, price) -> response
        .addItems(ProductPriceItem.newBuilder().setProductId(productId).setPrice(price.doubleValue()).build()));

    responseObserver.onNext(response.build());

    responseObserver.onCompleted();
  }
}
