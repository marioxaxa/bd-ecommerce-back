package com.ecommerce.domain.entrega;

import java.time.LocalDateTime;

public record Entrega(
    Integer id,
    Integer codigoRastreio,
    LocalDateTime previsao,
    String status,
    Integer transportadoraId,
    Integer enderecoId
) {}
