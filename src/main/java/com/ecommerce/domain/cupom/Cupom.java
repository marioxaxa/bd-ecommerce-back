package com.ecommerce.domain.cupom;

import java.time.LocalDateTime;

public record Cupom(
    Integer codigo,
    Float valor,
    Integer quantidade,
    LocalDateTime validade
) {}
