package com.market_place.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.market_place.order.dto.OrderRequest;
import com.market_place.order.dto.OrderResponse;
import com.market_place.order.mapper.OrderMapper;
import com.market_place.order.model.Order;
import com.market_place.order.model.OrderProduct;
import com.market_place.order.repository.OrderRepo;
import com.market_place.proto.inventory.InventoryServiceGrpc.InventoryServiceBlockingStub;
import com.market_place.proto.inventory.ProductAvailability;
import com.market_place.proto.inventory.ProductQuantity;
import com.market_place.proto.inventory.StockCheckRequest;
import com.market_place.proto.inventory.StockCheckResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final OrderMapper mapper;
  private final OrderRepo repo;

  private final InventoryServiceBlockingStub stub;

  public OrderResponse placeOrder(UUID customerID, OrderRequest request) {

    StockCheckRequest.Builder builder = StockCheckRequest.newBuilder();

    request.products().forEach(product -> builder.addItems(ProductQuantity.newBuilder()
        .setProductId(product.getProductId().toString()).setQuantity(product.getQuantity()).build()));

    StockCheckRequest gRequest = builder.build();

    StockCheckResponse gResponse = stub.checkAvailability(gRequest);

    Map<String, Boolean> availabilityMap = gResponse.getResultsList().stream()
        .collect(Collectors.toMap(ProductAvailability::getProductId, ProductAvailability::getAvailable));

    List<OrderProduct> savableProducts = new ArrayList<>();
    List<OrderProduct> responseProducts = new ArrayList<>();
    BigDecimal totalPrice = BigDecimal.ZERO;
    request.products().stream().forEach(product -> {
      boolean available = availabilityMap.getOrDefault(product.getProductId(), false);

      if (available) {
        OrderProduct orderProduct = new OrderProduct(product.getProductId(), product.getQuantity(), product.getPrice());
        totalPrice.add(product.getPrice());
        savableProducts.add(orderProduct);
        responseProducts.add(orderProduct);
      } else {
        // The price is gonne be fetched from the Pproduct service via grpc. these is
        // for demo
        responseProducts.add(new OrderProduct(product.getProductId(), 0, BigDecimal.ZERO));
      }
    });
    if (savableProducts.isEmpty()) {
      return new OrderResponse(null, responseProducts);
    }

    Order order = new Order();
    order.setCustomerId(customerID);
    order.setProducts(savableProducts);
    order.setTotalPrice(totalPrice);

    OrderResponse response = mapper.orderToResponse(repo.save(order));
    log.info("Order placed with id {}", response.id());
    return response;
  }

  public List<OrderResponse> getOrder(UUID customerID) {

    return repo.findByCustomerId(customerID).orElse(Collections.emptyList()).stream().map(mapper::orderToResponse)
        .toList();
  }
}
