package com.market_place.order.service;

import com.market_place.order.dto.OrderRequest;
import com.market_place.order.dto.OrderResponse;
import com.market_place.order.mapper.OrderMapper;
import com.market_place.order.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderMapper mapper;
    private final OrderRepo repo;

    public OrderResponse placeOrder(OrderRequest request){
        OrderResponse response = mapper.orderToResponse(repo.save(mapper.requestToOrder(request)));
        log.info("Order placed with id {}", response.id());
        return response;
    }
}
