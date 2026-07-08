package com.ecommerce.domain.produto;

public record Produto(
    Integer id,
    String marca,
    String nome,
    String fabricante,
    Integer categoriaId,
    String descricao,
    Long usuarioCpf
) {}
