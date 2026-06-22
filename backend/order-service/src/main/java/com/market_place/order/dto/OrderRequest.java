package com.market_place.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderRequest(UUID productId, Integer quantity, UUID customerId, BigDecimal totalPrice) {

}
