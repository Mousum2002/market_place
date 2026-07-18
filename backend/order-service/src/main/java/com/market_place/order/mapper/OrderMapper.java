package com.market_place.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.market_place.order.dto.OrderRequest;
import com.market_place.order.dto.OrderResponse;
import com.market_place.order.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  OrderResponse orderToResponse(Order order);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "customerId", ignore = true)
  Order requestToOrder(OrderRequest request);

}
