package com.ecommerce.domain.pagamento;

import java.time.LocalDateTime;

public record Pagamento(
    Integer id,
    Float valorTotal,
    LocalDateTime data,
    Integer formaDePagamentoId
) {}
