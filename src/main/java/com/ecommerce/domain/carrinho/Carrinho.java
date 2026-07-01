package com.ecommerce.domain.carrinho;

public record Carrinho(
    Integer id,
    String nome,
    String valorTotal,
    Integer usuarioCpf
) {}
