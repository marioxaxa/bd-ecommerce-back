package com.ecommerce.repository;

import com.ecommerce.domain.categoria.Categoria;
import com.ecommerce.domain.produto.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({ProdutoRepository.class, CategoriaRepository.class})
@ActiveProfiles("test")
@Sql(statements = {"DELETE FROM Produto", "DELETE FROM categoria"})
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria categoria;
    private Produto produto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Eletrônicos");
        categoriaRepository.save(categoria);
        produto = new Produto(1, "Samsung", "Galaxy S23", "Samsung Electronics", categoria.id(), "Smartphone", null);
    }

    @Test
    @DisplayName("Deve salvar e encontrar produto por ID")
    void deveSalvarEncontrarProduto() {
        produtoRepository.save(produto);

        Optional<Produto> result = produtoRepository.findById(produto.id());

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(produto.id());
        assertThat(result.get().marca()).isEqualTo(produto.marca());
        assertThat(result.get().nome()).isEqualTo(produto.nome());
        assertThat(result.get().fabricante()).isEqualTo(produto.fabricante());
        assertThat(result.get().categoriaId()).isEqualTo(produto.categoriaId());
    }

    @Test
    @DisplayName("Deve retornar produtos por categoria")
    void deveRetornarProdutosPorCategoria() {
        Produto produto1 = new Produto(1, "Samsung", "Galaxy S23", "Samsung", categoria.id(), "Smartphone", null);
        Produto produto2 = new Produto(2, "Apple", "iPhone 15", "Apple", categoria.id(), "Smartphone", null);
        
        produtoRepository.save(produto1);
        produtoRepository.save(produto2);

        List<Produto> result = produtoRepository.findByCategoriaId(categoria.id());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar todos os produtos")
    void deveRetornarTodosProdutos() {
        Categoria categoria2 = new Categoria(2, "Roupas");
        categoriaRepository.save(categoria2);
        
        Produto produto1 = new Produto(1, "Samsung", "Galaxy", "Samsung", categoria.id(), "Smartphone", null);
        Produto produto2 = new Produto(2, "Nike", "Camiseta", "Nike", categoria2.id(), "Camiseta", null);
        
        produtoRepository.save(produto1);
        produtoRepository.save(produto2);

        List<Produto> result = produtoRepository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve atualizar produto")
    void deveAtualizarProduto() {
        produtoRepository.save(produto);
        
        Produto produtoAtualizado = new Produto(produto.id(), "Apple", "iPhone", "Apple Inc.", categoria.id(), "Atualizado", null);
        produtoRepository.update(produtoAtualizado);

        Optional<Produto> result = produtoRepository.findById(produto.id());

        assertThat(result).isPresent();
        assertThat(result.get().marca()).isEqualTo("Apple");
        assertThat(result.get().nome()).isEqualTo("iPhone");
        assertThat(result.get().fabricante()).isEqualTo("Apple Inc.");
    }

    @Test
    @DisplayName("Deve deletar produto")
    void deveDeletarProduto() {
        produtoRepository.save(produto);
        
        int rowsAffected = produtoRepository.delete(produto.id());

        assertThat(rowsAffected).isEqualTo(1);
        
        Optional<Produto> result = produtoRepository.findById(produto.id());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve salvar e encontrar categoria")
    void deveSalvarEncontrarCategoria() {
        Optional<Categoria> result = categoriaRepository.findById(categoria.id());

        assertThat(result).isPresent();
        assertThat(result.get().descricao()).isEqualTo(categoria.descricao());
    }

    @Test
    @DisplayName("Deve retornar todas as categorias")
    void deveRetornarTodasCategorias() {
        Categoria categoria2 = new Categoria(2, "Roupas");
        categoriaRepository.save(categoria2);

        List<Categoria> result = categoriaRepository.findAll();

        assertThat(result).hasSize(2);
    }
}
