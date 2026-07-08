package com.ecommerce.repository;

import com.ecommerce.domain.carrinho.Carrinho;
import com.ecommerce.domain.cupom.Cupom;
import com.ecommerce.domain.endereco.Endereco;
import com.ecommerce.domain.entrega.Entrega;
import com.ecommerce.domain.pagamento.FormaDePagamento;
import com.ecommerce.domain.pagamento.Pagamento;
import com.ecommerce.domain.pedido.Pedido;
import com.ecommerce.domain.transportadora.Transportadora;
import com.ecommerce.domain.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({PedidoRepository.class, CupomRepository.class, PagamentoRepository.class, EntregaRepository.class, CarrinhoRepository.class, FormaDePagamentoRepository.class, EnderecoRepository.class, TransportadoraRepository.class, UsuarioRepository.class})
@ActiveProfiles("test")
@Sql(statements = {
    "DELETE FROM pedido",
    "DELETE FROM pagamento",
    "DELETE FROM entrega",
    "DELETE FROM carrinho",
    "DELETE FROM cupom",
    "DELETE FROM Forma_de_pagamento",
    "DELETE FROM endereco",
    "DELETE FROM transportadora",
    "DELETE FROM usuario"
})
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private FormaDePagamentoRepository formaDePagamentoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private TransportadoraRepository transportadoraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;
    private Endereco endereco;
    private Transportadora transportadora;
    private FormaDePagamento formaDePagamento;
    private Cupom cupom;
    private Carrinho carrinho;
    private Entrega entrega;
    private Pagamento pagamento;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(123456789L, "teste@email.com", "senha", "user", false);
        usuarioRepository.save(usuario);
        
        endereco = new Endereco(1, "Rua Teste", 123, "Centro", 12345678, "Casa", usuario.cpf());
        enderecoRepository.save(endereco);
        
        transportadora = new Transportadora(1, "Correios");
        transportadoraRepository.save(transportadora);
        
        formaDePagamento = new FormaDePagamento(1, 5, endereco.id());
        formaDePagamentoRepository.save(formaDePagamento);
        
        cupom = new Cupom("CUPOM100", 50.0f, 10, LocalDateTime.now().plusDays(30));
        cupomRepository.save(cupom);
        
        carrinho = new Carrinho(1, "Meu Carrinho", "500.00", usuario.cpf());
        carrinhoRepository.save(carrinho);
        
        entrega = new Entrega(1, 12345, LocalDateTime.now().plusDays(5), "Enviado", transportadora.id(), endereco.id());
        entregaRepository.save(entrega);
        
        pagamento = new Pagamento(1, 500.0f, LocalDateTime.now(), formaDePagamento.id());
        pagamentoRepository.save(pagamento);
        
        pedido = new Pedido(1000, cupom.codigo(), carrinho.id(), pagamento.id(), entrega.id());
    }

    @Test
    @DisplayName("Deve salvar e encontrar pedido por código")
    void deveSalvarEncontrarPedido() {
        pedidoRepository.save(pedido);

        Optional<Pedido> result = pedidoRepository.findById(pedido.codigo());

        assertThat(result).isPresent();
        assertThat(result.get().codigo()).isEqualTo(pedido.codigo());
        assertThat(result.get().cupomCodigo()).isEqualTo(pedido.cupomCodigo());
        assertThat(result.get().carrinhoId()).isEqualTo(pedido.carrinhoId());
        assertThat(result.get().pagamentoId()).isEqualTo(pedido.pagamentoId());
        assertThat(result.get().entregaId()).isEqualTo(pedido.entregaId());
    }

    @Test
    @DisplayName("Deve retornar todos os pedidos")
    void deveRetornarTodosPedidos() {
        pedidoRepository.save(pedido);

        List<Pedido> result = pedidoRepository.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve atualizar pedido")
    void deveAtualizarPedido() {
        pedidoRepository.save(pedido);
        
        Cupom novoCupom = new Cupom("CUPOM200", 100.0f, 5, LocalDateTime.now().plusDays(60));
        cupomRepository.save(novoCupom);
        
        Pedido pedidoAtualizado = new Pedido(pedido.codigo(), novoCupom.codigo(), pedido.carrinhoId(), pedido.pagamentoId(), pedido.entregaId());
        pedidoRepository.update(pedidoAtualizado);

        Optional<Pedido> result = pedidoRepository.findById(pedido.codigo());

        assertThat(result).isPresent();
        assertThat(result.get().cupomCodigo()).isEqualTo("CUPOM200");
    }

    @Test
    @DisplayName("Deve deletar pedido")
    void deveDeletarPedido() {
        pedidoRepository.save(pedido);
        
        int rowsAffected = pedidoRepository.delete(pedido.codigo());

        assertThat(rowsAffected).isEqualTo(1);
        
        Optional<Pedido> result = pedidoRepository.findById(pedido.codigo());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve salvar e encontrar cupom")
    void deveSalvarEncontrarCupom() {
        Optional<Cupom> result = cupomRepository.findById(cupom.codigo());

        assertThat(result).isPresent();
        assertThat(result.get().valor()).isEqualTo(cupom.valor());
        assertThat(result.get().quantidade()).isEqualTo(cupom.quantidade());
    }

    @Test
    @DisplayName("Deve salvar e encontrar pagamento")
    void deveSalvarEncontrarPagamento() {
        Optional<Pagamento> result = pagamentoRepository.findById(pagamento.id());

        assertThat(result).isPresent();
        assertThat(result.get().valorTotal()).isEqualTo(pagamento.valorTotal());
        assertThat(result.get().formaDePagamentoId()).isEqualTo(pagamento.formaDePagamentoId());
    }

    @Test
    @DisplayName("Deve salvar e encontrar entrega")
    void deveSalvarEncontrarEntrega() {
        Optional<Entrega> result = entregaRepository.findById(entrega.id());

        assertThat(result).isPresent();
        assertThat(result.get().codigoRastreio()).isEqualTo(entrega.codigoRastreio());
        assertThat(result.get().status()).isEqualTo(entrega.status());
        assertThat(result.get().transportadoraId()).isEqualTo(entrega.transportadoraId());
        assertThat(result.get().enderecoId()).isEqualTo(entrega.enderecoId());
    }

    @Test
    @DisplayName("Deve mapear corretamente campos DATETIME")
    void deveMapearCamposDatetime() {
        Optional<Cupom> cupomResult = cupomRepository.findById(cupom.codigo());
        Optional<Pagamento> pagamentoResult = pagamentoRepository.findById(pagamento.id());
        Optional<Entrega> entregaResult = entregaRepository.findById(entrega.id());

        assertThat(cupomResult).isPresent();
        assertThat(cupomResult.get().validade()).isNotNull();
        
        assertThat(pagamentoResult).isPresent();
        assertThat(pagamentoResult.get().data()).isNotNull();
        
        assertThat(entregaResult).isPresent();
        assertThat(entregaResult.get().previsao()).isNotNull();
    }
}
