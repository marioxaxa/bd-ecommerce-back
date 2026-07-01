package com.ecommerce.service;

import com.ecommerce.domain.cupom.Cupom;
import com.ecommerce.domain.entrega.Entrega;
import com.ecommerce.domain.pagamento.Pagamento;
import com.ecommerce.domain.pedido.Pedido;
import com.ecommerce.repository.CupomRepository;
import com.ecommerce.repository.EntregaRepository;
import com.ecommerce.repository.PagamentoRepository;
import com.ecommerce.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private CupomRepository cupomRepository;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private EntregaRepository entregaRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Cupom cupom;
    private Pagamento pagamento;
    private Entrega entrega;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        cupom = new Cupom(100, 50.0f, 10, LocalDateTime.now().plusDays(30));
        pagamento = new Pagamento(1, 500.0f, LocalDateTime.now(), 1);
        entrega = new Entrega(1, 12345, LocalDateTime.now().plusDays(5), "Enviado", 1, 1);
        pedido = new Pedido(1000, cupom.codigo(), 1, pagamento.id(), entrega.id());
    }

    @Test
    @DisplayName("Deve retornar lista de pedidos")
    void deveRetornarListaDePedidos() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<Pedido> result = pedidoService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).codigo()).isEqualTo(pedido.codigo());
    }

    @Test
    @DisplayName("Deve retornar pedido por código")
    void deveRetornarPedidoPorCodigo() {
        when(pedidoRepository.findById(pedido.codigo())).thenReturn(Optional.of(pedido));

        Optional<Pedido> result = pedidoService.findById(pedido.codigo());

        assertThat(result).isPresent();
        assertThat(result.get().codigo()).isEqualTo(pedido.codigo());
    }

    @Test
    @DisplayName("Deve salvar pedido com cupom válido")
    void deveSalvarPedido() {
        when(cupomRepository.findById(cupom.codigo())).thenReturn(Optional.of(cupom));
        when(pagamentoRepository.findById(pagamento.id())).thenReturn(Optional.of(pagamento));
        when(entregaRepository.findById(entrega.id())).thenReturn(Optional.of(entrega));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(1);

        Pedido result = pedidoService.save(pedido);

        assertThat(result).isEqualTo(pedido);
        verify(cupomRepository, times(2)).findById(cupom.codigo());
        verify(pagamentoRepository).findById(pagamento.id());
        verify(entregaRepository).findById(entrega.id());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar pedido com cupom inexistente")
    void deveLancarExcecaoAoSalvarPedidoComCupomInexistente() {
        when(cupomRepository.findById(999)).thenReturn(Optional.empty());
        Pedido pedidoInvalido = new Pedido(1000, 999, 1, pagamento.id(), entrega.id());

        assertThatThrownBy(() -> pedidoService.save(pedidoInvalido))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cupom não encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar pedido com pagamento inexistente")
    void deveLancarExcecaoAoSalvarPedidoComPagamentoInexistente() {
        when(cupomRepository.findById(cupom.codigo())).thenReturn(Optional.of(cupom));
        when(pagamentoRepository.findById(999)).thenReturn(Optional.empty());
        Pedido pedidoInvalido = new Pedido(1000, cupom.codigo(), 1, 999, entrega.id());

        assertThatThrownBy(() -> pedidoService.save(pedidoInvalido))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Pagamento não encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar pedido com entrega inexistente")
    void deveLancarExcecaoAoSalvarPedidoComEntregaInexistente() {
        when(cupomRepository.findById(cupom.codigo())).thenReturn(Optional.of(cupom));
        when(pagamentoRepository.findById(pagamento.id())).thenReturn(Optional.of(pagamento));
        when(entregaRepository.findById(999)).thenReturn(Optional.empty());
        Pedido pedidoInvalido = new Pedido(1000, cupom.codigo(), 1, pagamento.id(), 999);

        assertThatThrownBy(() -> pedidoService.save(pedidoInvalido))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Entrega não encontrada");
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar pedido com cupom esgotado")
    void deveLancarExcecaoAoSalvarPedidoComCupomEsgotado() {
        Cupom cupomEsgotado = new Cupom(cupom.codigo(), cupom.valor(), 0, cupom.validade());
        when(cupomRepository.findById(cupom.codigo())).thenReturn(Optional.of(cupomEsgotado));
        when(pagamentoRepository.findById(pagamento.id())).thenReturn(Optional.of(pagamento));
        when(entregaRepository.findById(entrega.id())).thenReturn(Optional.of(entrega));

        assertThatThrownBy(() -> pedidoService.save(pedido))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cupom esgotado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar pedido com cupom expirado")
    void deveLancarExcecaoAoSalvarPedidoComCupomExpirado() {
        Cupom cupomExpirado = new Cupom(cupom.codigo(), cupom.valor(), cupom.quantidade(), LocalDateTime.now().minusDays(1));
        when(cupomRepository.findById(cupom.codigo())).thenReturn(Optional.of(cupomExpirado));
        when(pagamentoRepository.findById(pagamento.id())).thenReturn(Optional.of(pagamento));
        when(entregaRepository.findById(entrega.id())).thenReturn(Optional.of(entrega));

        assertThatThrownBy(() -> pedidoService.save(pedido))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cupom expirado");
    }

    @Test
    @DisplayName("Deve atualizar pedido existente")
    void deveAtualizarPedido() {
        when(pedidoRepository.findById(pedido.codigo())).thenReturn(Optional.of(pedido));
        when(cupomRepository.findById(cupom.codigo())).thenReturn(Optional.of(cupom));
        when(pagamentoRepository.findById(pagamento.id())).thenReturn(Optional.of(pagamento));
        when(entregaRepository.findById(entrega.id())).thenReturn(Optional.of(entrega));
        when(pedidoRepository.update(any(Pedido.class))).thenReturn(1);

        Pedido result = pedidoService.update(pedido.codigo(), pedido);

        assertThat(result.codigo()).isEqualTo(pedido.codigo());
    }

    @Test
    @DisplayName("Deve deletar pedido existente")
    void deveDeletarPedido() {
        when(pedidoRepository.findById(pedido.codigo())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.delete(pedido.codigo())).thenReturn(1);

        pedidoService.delete(pedido.codigo());

        verify(pedidoRepository).findById(pedido.codigo());
        verify(pedidoRepository).delete(pedido.codigo());
    }

    @Test
    @DisplayName("Deve retornar lista de cupons")
    void deveRetornarListaDeCupons() {
        when(cupomRepository.findAll()).thenReturn(List.of(cupom));

        List<Cupom> result = pedidoService.findAllCupons();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).codigo()).isEqualTo(cupom.codigo());
    }

    @Test
    @DisplayName("Deve salvar cupom")
    void deveSalvarCupom() {
        when(cupomRepository.save(any(Cupom.class))).thenReturn(1);

        Cupom result = pedidoService.saveCupom(cupom);

        assertThat(result).isEqualTo(cupom);
    }

    @Test
    @DisplayName("Deve retornar lista de pagamentos")
    void deveRetornarListaDePagamentos() {
        when(pagamentoRepository.findAll()).thenReturn(List.of(pagamento));

        List<Pagamento> result = pedidoService.findAllPagamentos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(pagamento.id());
    }

    @Test
    @DisplayName("Deve salvar pagamento")
    void deveSalvarPagamento() {
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(1);

        Pagamento result = pedidoService.savePagamento(pagamento);

        assertThat(result).isEqualTo(pagamento);
    }

    @Test
    @DisplayName("Deve retornar lista de entregas")
    void deveRetornarListaDeEntregas() {
        when(entregaRepository.findAll()).thenReturn(List.of(entrega));

        List<Entrega> result = pedidoService.findAllEntregas();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(entrega.id());
    }

    @Test
    @DisplayName("Deve salvar entrega")
    void deveSalvarEntrega() {
        when(entregaRepository.save(any(Entrega.class))).thenReturn(1);

        Entrega result = pedidoService.saveEntrega(entrega);

        assertThat(result).isEqualTo(entrega);
    }
}
