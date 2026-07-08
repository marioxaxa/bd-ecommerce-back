package com.ecommerce.domain.pagamento;

import java.time.LocalDateTime;

public record Cartao(
    Integer numero,
    Integer formaDePagamentoId,
    String titular,
    LocalDateTime validade,
    Integer cvv,
    String bandeira,
    Integer numeroParcelas
) {}
