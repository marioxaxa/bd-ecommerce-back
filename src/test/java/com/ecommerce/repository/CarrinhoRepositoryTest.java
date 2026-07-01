package com.ecommerce.repository;

import com.ecommerce.domain.carrinho.Carrinho;
import com.ecommerce.domain.carrinho.Contem;
import com.ecommerce.domain.categoria.Categoria;
import com.ecommerce.domain.produto.Produto;
import com.ecommerce.domain.usuario.Usuario;
import com.ecommerce.domain.variacao.Variacao;
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
@Import({CarrinhoRepository.class, ContemRepository.class, VariacaoRepository.class, ProdutoRepository.class, CategoriaRepository.class, UsuarioRepository.class})
@ActiveProfiles("test")
@Sql(statements = {"DELETE FROM contem", "DELETE FROM carrinho", "DELETE FROM Variacao", "DELETE FROM Produto", "DELETE FROM categoria", "DELETE FROM usuario"})
class CarrinhoRepositoryTest {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ContemRepository contemRepository;

    @Autowired
    private VariacaoRepository variacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;
    private Carrinho carrinho;
    private Variacao variacao;
    private Contem contem;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(123456789, "teste@email.com", "senha", "user", false);
        usuarioRepository.save(usuario);
        
        carrinho = new Carrinho(1, "Meu Carrinho", "0.0", usuario.cpf());
        
        Categoria categoria = new Categoria(1, "Eletrônicos");
        categoriaRepository.save(categoria);
        
        Produto produto = new Produto(1, "Samsung", "Galaxy", "Samsung", categoria.id());
        produtoRepository.save(produto);
        
        variacao = new Variacao(1, "128GB", 4999.99f, 10, "foto.jpg", produto.id());
        variacaoRepository.save(variacao);
        
        contem = new Contem(variacao.id(), carrinho.id(), 2);
    }

    @Test
    @DisplayName("Deve salvar e encontrar carrinho por ID")
    void deveSalvarEncontrarCarrinho() {
        carrinhoRepository.save(carrinho);

        Optional<Carrinho> result = carrinhoRepository.findById(carrinho.id());

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(carrinho.id());
        assertThat(result.get().nome()).isEqualTo(carrinho.nome());
        assertThat(result.get().valorTotal()).isEqualTo(carrinho.valorTotal());
        assertThat(result.get().usuarioCpf()).isEqualTo(carrinho.usuarioCpf());
    }

    @Test
    @DisplayName("Deve retornar carrinhos por usuário")
    void deveRetornarCarrinhosPorUsuario() {
        Carrinho carrinho1 = new Carrinho(1, "Carrinho 1", "0.0", usuario.cpf());
        Carrinho carrinho2 = new Carrinho(2, "Carrinho 2", "0.0", usuario.cpf());
        
        carrinhoRepository.save(carrinho1);
        carrinhoRepository.save(carrinho2);

        List<Carrinho> result = carrinhoRepository.findByUsuarioCpf(usuario.cpf());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve atualizar carrinho")
    void deveAtualizarCarrinho() {
        carrinhoRepository.save(carrinho);
        
        Carrinho carrinhoAtualizado = new Carrinho(carrinho.id(), "Novo Nome", "500.00", carrinho.usuarioCpf());
        carrinhoRepository.update(carrinhoAtualizado);

        Optional<Carrinho> result = carrinhoRepository.findById(carrinho.id());

        assertThat(result).isPresent();
        assertThat(result.get().nome()).isEqualTo("Novo Nome");
        assertThat(result.get().valorTotal()).isEqualTo("500.00");
    }

    @Test
    @DisplayName("Deve deletar carrinho")
    void deveDeletarCarrinho() {
        carrinhoRepository.save(carrinho);
        
        int rowsAffected = carrinhoRepository.delete(carrinho.id());

        assertThat(rowsAffected).isEqualTo(1);
        
        Optional<Carrinho> result = carrinhoRepository.findById(carrinho.id());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve salvar e encontrar item do carrinho")
    void deveSalvarEncontrarItemCarrinho() {
        carrinhoRepository.save(carrinho);
        contemRepository.save(contem);

        List<Contem> result = contemRepository.findByCarrinhoId(carrinho.id());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).variacaoId()).isEqualTo(variacao.id());
        assertThat(result.get(0).carrinhoId()).isEqualTo(carrinho.id());
        assertThat(result.get(0).quantidade()).isEqualTo(contem.quantidade());
    }

    @Test
    @DisplayName("Deve retornar itens por variação")
    void deveRetornarItensPorVariacao() {
        carrinhoRepository.save(carrinho);
        contemRepository.save(contem);

        List<Contem> result = contemRepository.findByVariacaoId(variacao.id());

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve atualizar quantidade do item")
    void deveAtualizarQuantidadeItem() {
        carrinhoRepository.save(carrinho);
        contemRepository.save(contem);
        
        Contem contemAtualizado = new Contem(variacao.id(), carrinho.id(), 5);
        contemRepository.update(contemAtualizado);

        List<Contem> result = contemRepository.findByCarrinhoId(carrinho.id());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).quantidade()).isEqualTo(5);
    }

    @Test
    @DisplayName("Deve remover item do carrinho")
    void deveRemoverItemDoCarrinho() {
        carrinhoRepository.save(carrinho);
        contemRepository.save(contem);
        
        int rowsAffected = contemRepository.delete(variacao.id(), carrinho.id());

        assertThat(rowsAffected).isEqualTo(1);
        
        List<Contem> result = contemRepository.findByCarrinhoId(carrinho.id());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve mapear corretamente a coluna quanatidade (com typo)")
    void deveMapearColunaQuanatidade() {
        carrinhoRepository.save(carrinho);
        contemRepository.save(contem);

        List<Contem> result = contemRepository.findByCarrinhoId(carrinho.id());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).quantidade()).isEqualTo(2);
    }
}
