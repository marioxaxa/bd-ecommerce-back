package com.ecommerce.domain.endereco;

public record Endereco(
    Integer id,
    String logradouro,
    Integer numero,
    String bairro,
    Integer cep,
    String nome,
    Long usuarioCpf
) {}
