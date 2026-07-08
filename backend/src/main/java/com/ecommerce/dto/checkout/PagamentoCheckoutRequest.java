package com.ecommerce.dto.checkout;

public record PagamentoCheckoutRequest(
    String tipo,
    String titular,
    Integer numero,
    Integer cvv,
    String bandeira,
    Integer numeroParcelas
) {}
