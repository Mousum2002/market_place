package com.marketplace.customer.dto;

import java.util.UUID;

public record CustomerResponse(Boolean exists, UUID userId, String username, String passwordHash) {

}
