package com.ecommerce.domain.preco;

import java.time.LocalDateTime;

public record PrecoHistorico(
    Integer id,
    Float valor,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    Integer variacaoId
) {}
