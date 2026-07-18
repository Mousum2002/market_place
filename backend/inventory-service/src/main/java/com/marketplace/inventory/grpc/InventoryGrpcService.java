package com.marketplace.inventory.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.grpc.server.service.GrpcService;

import com.market_place.proto.inventory.InventoryServiceGrpc;
import com.market_place.proto.inventory.ProductAvailability;
import com.market_place.proto.inventory.StockCheckRequest;
import com.market_place.proto.inventory.StockCheckResponse;
import com.marketplace.inventory.dto.ProductQuantityRequest;
import com.marketplace.inventory.repo.InventoryBulkRepo;

import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcService extends InventoryServiceGrpc.InventoryServiceImplBase {

  private final InventoryBulkRepo bulkRepo;

  @Override
  @Transactional
  public void checkAvailability(
      StockCheckRequest request,
      StreamObserver<StockCheckResponse> responseObserver) {
    List<ProductQuantityRequest> products = new ArrayList<>();

    request.getItemsList().forEach(item -> {
      products.add(new ProductQuantityRequest(
          UUID.fromString(item.getProductId()),
          item.getQuantity()));
    });

    Set<UUID> updatedProductIds = bulkRepo.subtractIfAvailable(products);

    StockCheckResponse.Builder response = StockCheckResponse.newBuilder();

    for (ProductQuantityRequest product : products) {
      response.addResults(
          ProductAvailability.newBuilder()
              .setProductId(product.productID().toString())
              .setAvailable(updatedProductIds.contains(product.productID()))
              .build());
    }

    responseObserver.onNext(response.build());
    responseObserver.onCompleted();
  }
}
