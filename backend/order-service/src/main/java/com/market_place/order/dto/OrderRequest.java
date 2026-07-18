package com.market_place.order.dto;

import java.util.List;

import com.market_place.order.model.OrderProduct;

public record OrderRequest(List<OrderProduct> products) {

}
