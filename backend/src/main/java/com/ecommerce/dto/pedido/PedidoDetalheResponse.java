package com.ecommerce.dto.pedido;

import com.ecommerce.domain.carrinho.Carrinho;
import com.ecommerce.domain.cupom.Cupom;
import com.ecommerce.domain.endereco.Endereco;
import com.ecommerce.domain.entrega.Entrega;
import com.ecommerce.domain.pagamento.Pagamento;
import com.ecommerce.domain.pedido.Pedido;
import com.ecommerce.domain.usuario.Usuario;

import java.util.List;

public record PedidoDetalheResponse(
    Pedido pedido,
    Usuario cliente,
    Carrinho carrinho,
    Endereco endereco,
    Entrega entrega,
    Pagamento pagamento,
    Cupom cupom,
    List<PedidoItemDetalhe> itens,
    Float subtotal,
    Float desconto,
    Float total
) {}
