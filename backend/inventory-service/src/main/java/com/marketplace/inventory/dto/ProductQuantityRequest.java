package com.marketplace.inventory.dto;

import java.util.UUID;

public record ProductQuantityRequest(UUID productID, Integer quantity) {

}
