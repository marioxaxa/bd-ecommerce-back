package com.ecommerce.domain.usuario;

public record Usuario(
    Integer cpf,
    String email,
    String senha,
    String username,
    Boolean gerente
) {}
