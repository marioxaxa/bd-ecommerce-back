package com.ecommerce.domain.pagamento;

public record FormaDePagamento(
    Integer id,
    Integer custoDeOperacao,
    Integer enderecoId
) {}
