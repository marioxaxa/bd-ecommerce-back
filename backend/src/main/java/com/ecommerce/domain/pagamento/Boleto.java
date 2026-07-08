package com.ecommerce.domain.pagamento;

public record Boleto(
    Integer codigoDeBarras,
    Integer formaDePagamentoId
) {}
