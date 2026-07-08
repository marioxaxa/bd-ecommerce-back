package com.ecommerce.dto.checkout;

public record CheckoutItemRequest(
    Integer variacaoId,
    Integer quantidade
) {}
