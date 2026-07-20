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
import com.marketplace.proto.product.ProductPriceRequest;
import com.marketplace.proto.product.ProductPriceResponse;
import com.marketplace.proto.product.ProductServiceGrpc.ProductServiceBlockingStub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final OrderMapper mapper;
  private final OrderRepo repo;
  private final InventoryServiceBlockingStub inventoryStub;

  private final ProductServiceBlockingStub productStub;

  public OrderResponse placeOrder(UUID customerID, OrderRequest request) {

    StockCheckRequest.Builder builder = StockCheckRequest.newBuilder();

    request.products().forEach(product -> builder.addItems(ProductQuantity.newBuilder()
        .setProductId(product.getProductId().toString()).setQuantity(product.getQuantity()).build()));

    StockCheckRequest gRequest = builder.build();

    StockCheckResponse gResponse = inventoryStub.checkAvailability(gRequest);

    Map<String, Boolean> availabilityMap = gResponse.getResultsList().stream()
        .collect(Collectors.toMap(ProductAvailability::getProductId, ProductAvailability::getAvailable));

    List<OrderProduct> savableProducts = new ArrayList<>();
    List<OrderProduct> responseProducts = new ArrayList<>();
    ProductPriceResponse priceResponse = productStub.getPrices(ProductPriceRequest.newBuilder()
        .addAllProductIds(request.products().stream().map(OrderProduct::getProductId).map(UUID::toString).toList())
        .build());
    Map<UUID, BigDecimal> priceMap = priceResponse.getItemsList().stream()
        .collect(Collectors.toMap(
            item -> UUID.fromString(item.getProductId()),
            item -> BigDecimal.valueOf(item.getPrice())));
    BigDecimal totalPrice = BigDecimal.ZERO;

    for (OrderProduct product : request.products()) {
      boolean available = availabilityMap.getOrDefault(product.getProductId().toString(), false);

      if (available) {
        OrderProduct orderProduct = new OrderProduct(product.getProductId(), product.getQuantity(),
            priceMap.get(product.getProductId()));
        totalPrice = totalPrice.add(orderProduct.getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity())));
        savableProducts.add(orderProduct);
        responseProducts.add(orderProduct);
      } else {
        responseProducts.add(new OrderProduct(product.getProductId(), 0, BigDecimal.ZERO));
      }
    }
    ;
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
