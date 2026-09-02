package com.marketplace.customer.dto;

import java.util.UUID;

public record CustomerContactDto(Boolean exists, UUID userId, String username, String email) {
}
