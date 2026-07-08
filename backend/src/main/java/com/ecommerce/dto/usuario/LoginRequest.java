package com.ecommerce.dto.usuario;

public record LoginRequest(
    String email,
    String senha
) {}
