package com.market_place.order.mapper;

import com.market_place.order.dto.OrderRequest;
import com.market_place.order.dto.OrderResponse;
import com.market_place.order.model.Order;
import com.market_place.order.model.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse orderToResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "products", expression = "java(mapProducts(request))")
    Order requestToOrder(OrderRequest request);

    default List<OrderProduct> mapProducts(OrderRequest request) {
        OrderProduct product = new OrderProduct();
        product.setProductId(request.productId());
        product.setQuantity(request.quantity());
        return List.of(product);
    }


}
