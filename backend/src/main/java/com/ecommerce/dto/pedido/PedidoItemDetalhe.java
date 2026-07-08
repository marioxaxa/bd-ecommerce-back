package com.ecommerce.dto.pedido;

import com.ecommerce.domain.produto.Produto;
import com.ecommerce.domain.variacao.Variacao;

public record PedidoItemDetalhe(
    Produto produto,
    Variacao variacao,
    Integer quantidade,
    Float subtotal
) {}
