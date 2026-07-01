package com.ecommerce.domain.pedido;

public record Pedido(
    Integer codigo,
    Integer cupomCodigo,
    Integer carrinhoId,
    Integer pagamentoId,
    Integer entregaId
) {}
