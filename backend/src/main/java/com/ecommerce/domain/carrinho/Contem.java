package com.ecommerce.domain.carrinho;

public record Contem(
    Integer variacaoId,
    Integer carrinhoId,
    Integer quantidade
) {}
