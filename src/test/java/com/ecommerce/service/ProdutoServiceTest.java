package com.ecommerce.service;

import com.ecommerce.domain.categoria.Categoria;
import com.ecommerce.domain.produto.Produto;
import com.ecommerce.domain.variacao.Variacao;
import com.ecommerce.repository.CategoriaRepository;
import com.ecommerce.repository.ProdutoRepository;
import com.ecommerce.repository.VariacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private VariacaoRepository variacaoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Categoria categoria;
    private Produto produto;
    private Variacao variacao;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Eletrônicos");
        produto = new Produto(1, "Samsung", "Galaxy S23", "Samsung Electronics", categoria.id());
        variacao = new Variacao(1, "128GB Preto", 4999.99f, 10, "url_foto.jpg", produto.id());
    }

    @Test
    @DisplayName("Deve retornar lista de produtos")
    void deveRetornarListaDeProdutos() {
        when(produtoRepository.findAll()).thenReturn(List.of(produto));

        List<Produto> result = produtoService.findAllProdutos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(produto.id());
    }

    @Test
    @DisplayName("Deve retornar produto por ID")
    void deveRetornarProdutoPorId() {
        when(produtoRepository.findById(produto.id())).thenReturn(Optional.of(produto));

        Optional<Produto> result = produtoService.findProdutoById(produto.id());

        assertThat(result).isPresent();
        assertThat(result.get().nome()).isEqualTo(produto.nome());
    }

    @Test
    @DisplayName("Deve retornar produtos por categoria")
    void deveRetornarProdutosPorCategoria() {
        when(categoriaRepository.findById(categoria.id())).thenReturn(Optional.of(categoria));
        when(produtoRepository.findByCategoriaId(categoria.id())).thenReturn(List.of(produto));

        List<Produto> result = produtoService.findProdutosByCategoria(categoria.id());

        assertThat(result).hasSize(1);
        verify(categoriaRepository).findById(categoria.id());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar produtos de categoria inexistente")
    void deveLancarExcecaoAoBuscarProdutosDeCategoriaInexistente() {
        when(categoriaRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.findProdutosByCategoria(999))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Categoria não encontrada");
    }

    @Test
    @DisplayName("Deve salvar produto com categoria válida")
    void deveSalvarProduto() {
        when(categoriaRepository.findById(categoria.id())).thenReturn(Optional.of(categoria));
        when(produtoRepository.save(any(Produto.class))).thenReturn(1);

        Produto result = produtoService.saveProduto(produto);

        assertThat(result).isEqualTo(produto);
        verify(categoriaRepository).findById(categoria.id());
        verify(produtoRepository).save(produto);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar produto com categoria inexistente")
    void deveLancarExcecaoAoSalvarProdutoComCategoriaInexistente() {
        when(categoriaRepository.findById(999)).thenReturn(Optional.empty());
        Produto produtoInvalido = new Produto(1, "Marca", "Nome", "Fab", 999);

        assertThatThrownBy(() -> produtoService.saveProduto(produtoInvalido))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Categoria não encontrada");
    }

    @Test
    @DisplayName("Deve atualizar produto existente")
    void deveAtualizarProduto() {
        Produto produtoAtualizado = new Produto(produto.id(), "NovaMarca", "NovoNome", "NovoFab", categoria.id());
        
        when(produtoRepository.findById(produto.id())).thenReturn(Optional.of(produto));
        when(categoriaRepository.findById(categoria.id())).thenReturn(Optional.of(categoria));
        when(produtoRepository.update(any(Produto.class))).thenReturn(1);

        Produto result = produtoService.updateProduto(produto.id(), produtoAtualizado);

        assertThat(result.id()).isEqualTo(produto.id());
        assertThat(result.marca()).isEqualTo(produtoAtualizado.marca());
    }

    @Test
    @DisplayName("Deve deletar produto existente")
    void deveDeletarProduto() {
        when(produtoRepository.findById(produto.id())).thenReturn(Optional.of(produto));
        when(produtoRepository.delete(produto.id())).thenReturn(1);

        produtoService.deleteProduto(produto.id());

        verify(produtoRepository).findById(produto.id());
        verify(produtoRepository).delete(produto.id());
    }

    @Test
    @DisplayName("Deve retornar categorias")
    void deveRetornarCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> result = produtoService.findAllCategorias();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).descricao()).isEqualTo(categoria.descricao());
    }

    @Test
    @DisplayName("Deve salvar categoria")
    void deveSalvarCategoria() {
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(1);

        Categoria result = produtoService.saveCategoria(categoria);

        assertThat(result).isEqualTo(categoria);
    }

    @Test
    @DisplayName("Deve retornar variações de produto")
    void deveRetornarVariacoesDeProduto() {
        when(produtoRepository.findById(produto.id())).thenReturn(Optional.of(produto));
        when(variacaoRepository.findByProdutoId(produto.id())).thenReturn(List.of(variacao));

        List<Variacao> result = produtoService.findVariacoesByProduto(produto.id());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nome()).isEqualTo(variacao.nome());
    }

    @Test
    @DisplayName("Deve salvar variação com produto válido")
    void deveSalvarVariacao() {
        when(produtoRepository.findById(produto.id())).thenReturn(Optional.of(produto));
        when(variacaoRepository.save(any(Variacao.class))).thenReturn(1);

        Variacao result = produtoService.saveVariacao(variacao);

        assertThat(result).isEqualTo(variacao);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar variação com produto inexistente")
    void deveLancarExcecaoAoSalvarVariacaoComProdutoInexistente() {
        when(produtoRepository.findById(999)).thenReturn(Optional.empty());
        Variacao variacaoInvalida = new Variacao(1, "Teste", 100f, 5, "foto.jpg", 999);

        assertThatThrownBy(() -> produtoService.saveVariacao(variacaoInvalida))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Produto não encontrado");
    }
}
