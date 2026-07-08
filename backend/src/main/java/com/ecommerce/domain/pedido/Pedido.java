package com.ecommerce.domain.pedido;

public record Pedido(
    Integer codigo,
    String cupomCodigo,
    Integer carrinhoId,
    Integer pagamentoId,
    Integer entregaId
) {}
