package com.ecommerce.domain.usuario;

public record Usuario(
    Long cpf,
    String email,
    String senha,
    String username,
    Boolean gerente
) {}
