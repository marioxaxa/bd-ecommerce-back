package com.ecommerce.domain.variacao;

public record Variacao(
    Integer id,
    String nome,
    Float preco,
    Integer estoque,
    String fotoUrl,
    Integer produtoId
) {}
