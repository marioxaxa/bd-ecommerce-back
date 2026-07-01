package com.ecommerce.service;

import com.ecommerce.domain.carrinho.Carrinho;
import com.ecommerce.domain.carrinho.Contem;
import com.ecommerce.domain.variacao.Variacao;
import com.ecommerce.repository.CarrinhoRepository;
import com.ecommerce.repository.ContemRepository;
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
class CarrinhoServiceTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private ContemRepository contemRepository;

    @Mock
    private VariacaoRepository variacaoRepository;

    @InjectMocks
    private CarrinhoService carrinhoService;

    private Carrinho carrinho;
    private Variacao variacao;
    private Contem contem;

    @BeforeEach
    void setUp() {
        carrinho = new Carrinho(1, "Meu Carrinho", "0.0", 123456789);
        variacao = new Variacao(1, "128GB Preto", 4999.99f, 10, "url_foto.jpg", 1);
        contem = new Contem(variacao.id(), carrinho.id(), 2);
    }

    @Test
    @DisplayName("Deve retornar lista de carrinhos")
    void deveRetornarListaDeCarrinhos() {
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));

        List<Carrinho> result = carrinhoService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(carrinho.id());
    }

    @Test
    @DisplayName("Deve retornar carrinho por ID")
    void deveRetornarCarrinhoPorId() {
        when(carrinhoRepository.findById(carrinho.id())).thenReturn(Optional.of(carrinho));

        Optional<Carrinho> result = carrinhoService.findById(carrinho.id());

        assertThat(result).isPresent();
        assertThat(result.get().nome()).isEqualTo(carrinho.nome());
    }

    @Test
    @DisplayName("Deve retornar carrinhos por usuário")
    void deveRetornarCarrinhosPorUsuario() {
        when(carrinhoRepository.findByUsuarioCpf(carrinho.usuarioCpf())).thenReturn(List.of(carrinho));

        List<Carrinho> result = carrinhoService.findByUsuario(carrinho.usuarioCpf());

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve salvar carrinho")
    void deveSalvarCarrinho() {
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(1);

        Carrinho result = carrinhoService.save(carrinho);

        assertThat(result).isEqualTo(carrinho);
        verify(carrinhoRepository).save(carrinho);
    }

    @Test
    @DisplayName("Deve atualizar carrinho existente")
    void deveAtualizarCarrinho() {
        Carrinho carrinhoAtualizado = new Carrinho(carrinho.id(), "Novo Nome", "100.0", carrinho.usuarioCpf());
        
        when(carrinhoRepository.findById(carrinho.id())).thenReturn(Optional.of(carrinho));
        when(carrinhoRepository.update(any(Carrinho.class))).thenReturn(1);

        Carrinho result = carrinhoService.update(carrinho.id(), carrinhoAtualizado);

        assertThat(result.id()).isEqualTo(carrinho.id());
        assertThat(result.nome()).isEqualTo(carrinhoAtualizado.nome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar carrinho inexistente")
    void deveLancarExcecaoAoAtualizarCarrinhoInexistente() {
        when(carrinhoRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carrinhoService.update(999, carrinho))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Carrinho não encontrado");
    }

    @Test
    @DisplayName("Deve deletar carrinho existente")
    void deveDeletarCarrinho() {
        when(carrinhoRepository.findById(carrinho.id())).thenReturn(Optional.of(carrinho));
        when(carrinhoRepository.delete(carrinho.id())).thenReturn(1);

        carrinhoService.delete(carrinho.id());

        verify(carrinhoRepository).findById(carrinho.id());
        verify(carrinhoRepository).delete(carrinho.id());
    }

    @Test
    @DisplayName("Deve retornar itens do carrinho")
    void deveRetornarItensDoCarrinho() {
        when(carrinhoRepository.findById(carrinho.id())).thenReturn(Optional.of(carrinho));
        when(contemRepository.findByCarrinhoId(carrinho.id())).thenReturn(List.of(contem));

        List<Contem> result = carrinhoService.findItensByCarrinho(carrinho.id());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).quantidade()).isEqualTo(contem.quantidade());
    }

    @Test
    @DisplayName("Deve adicionar item ao carrinho com estoque suficiente")
    void deveAdicionarItemAoCarrinho() {
        when(carrinhoRepository.findById(carrinho.id())).thenReturn(Optional.of(carrinho));
        when(variacaoRepository.findById(variacao.id())).thenReturn(Optional.of(variacao));
        when(contemRepository.save(any(Contem.class))).thenReturn(1);

        Contem result = carrinhoService.addItem(contem);

        assertThat(result).isEqualTo(contem);
        verify(contemRepository).save(contem);
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar item com estoque insuficiente")
    void deveLancarExcecaoAoAdicionarItemComEstoqueInsuficiente() {
        Variacao variacaoSemEstoque = new Variacao(variacao.id(), variacao.nome(), variacao.preco(), 1, variacao.fotoUrl(), variacao.produtoId());
        Contem contemComQuantidadeAlta = new Contem(variacao.id(), carrinho.id(), 5);
        
        when(carrinhoRepository.findById(carrinho.id())).thenReturn(Optional.of(carrinho));
        when(variacaoRepository.findById(variacao.id())).thenReturn(Optional.of(variacaoSemEstoque));

        assertThatThrownBy(() -> carrinhoService.addItem(contemComQuantidadeAlta))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Estoque insuficiente");
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar item com carrinho inexistente")
    void deveLancarExcecaoAoAdicionarItemComCarrinhoInexistente() {
        when(carrinhoRepository.findById(999)).thenReturn(Optional.empty());
        Contem contemComCarrinhoInexistente = new Contem(variacao.id(), 999, 1);

        assertThatThrownBy(() -> carrinhoService.addItem(contemComCarrinhoInexistente))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Carrinho não encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar item com variação inexistente")
    void deveLancarExcecaoAoAdicionarItemComVariacaoInexistente() {
        when(carrinhoRepository.findById(carrinho.id())).thenReturn(Optional.of(carrinho));
        when(variacaoRepository.findById(999)).thenReturn(Optional.empty());
        Contem contemInvalido = new Contem(999, carrinho.id(), 1);

        assertThatThrownBy(() -> carrinhoService.addItem(contemInvalido))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Variação não encontrada");
    }

    @Test
    @DisplayName("Deve atualizar item do carrinho")
    void deveAtualizarItemDoCarrinho() {
        when(variacaoRepository.findById(variacao.id())).thenReturn(Optional.of(variacao));
        when(carrinhoRepository.findById(carrinho.id())).thenReturn(Optional.of(carrinho));
        when(contemRepository.update(any(Contem.class))).thenReturn(1);

        Contem result = carrinhoService.updateItem(variacao.id(), carrinho.id(), 3);

        assertThat(result.quantidade()).isEqualTo(3);
    }

    @Test
    @DisplayName("Deve remover item do carrinho")
    void deveRemoverItemDoCarrinho() {
        when(contemRepository.delete(variacao.id(), carrinho.id())).thenReturn(1);

        carrinhoService.removeItem(variacao.id(), carrinho.id());

        verify(contemRepository).delete(variacao.id(), carrinho.id());
    }
}
