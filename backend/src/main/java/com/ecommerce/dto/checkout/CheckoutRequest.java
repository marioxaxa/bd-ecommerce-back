package com.ecommerce.dto.checkout;

import com.ecommerce.domain.endereco.Endereco;

import java.util.List;

public record CheckoutRequest(
    Long usuarioCpf,
    Integer enderecoId,
    Endereco novoEndereco,
    String cupomCodigo,
    PagamentoCheckoutRequest pagamento,
    List<CheckoutItemRequest> itens
) {}
