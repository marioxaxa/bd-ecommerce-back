package com.ecommerce.dto.produto;

import com.ecommerce.domain.atributo.Atributo;
import com.ecommerce.domain.categoria.Categoria;
import com.ecommerce.domain.produto.Produto;
import com.ecommerce.domain.variacao.Variacao;

import java.util.List;

public record ProdutoCompletoRequest(
    Produto produto,
    Categoria categoria,
    List<Atributo> atributos,
    List<Variacao> variacoes
) {}
