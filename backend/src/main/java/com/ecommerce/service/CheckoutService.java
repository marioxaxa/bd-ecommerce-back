package com.ecommerce.service;

import com.ecommerce.domain.carrinho.Carrinho;
import com.ecommerce.domain.carrinho.Contem;
import com.ecommerce.domain.cupom.Cupom;
import com.ecommerce.domain.endereco.Endereco;
import com.ecommerce.domain.entrega.Entrega;
import com.ecommerce.domain.pagamento.Boleto;
import com.ecommerce.domain.pagamento.Cartao;
import com.ecommerce.domain.pagamento.FormaDePagamento;
import com.ecommerce.domain.pagamento.Pagamento;
import com.ecommerce.domain.pagamento.Pix;
import com.ecommerce.domain.pedido.Pedido;
import com.ecommerce.domain.produto.Produto;
import com.ecommerce.domain.transportadora.Transportadora;
import com.ecommerce.domain.usuario.Usuario;
import com.ecommerce.domain.variacao.Variacao;
import com.ecommerce.dto.checkout.CheckoutItemRequest;
import com.ecommerce.dto.checkout.CheckoutRequest;
import com.ecommerce.dto.checkout.CheckoutResponse;
import com.ecommerce.dto.checkout.PagamentoCheckoutRequest;
import com.ecommerce.dto.pedido.PedidoDetalheResponse;
import com.ecommerce.dto.pedido.PedidoItemDetalhe;
import com.ecommerce.repository.BoletoRepository;
import com.ecommerce.repository.CarrinhoRepository;
import com.ecommerce.repository.CartaoRepository;
import com.ecommerce.repository.ContemRepository;
import com.ecommerce.repository.CupomRepository;
import com.ecommerce.repository.EnderecoRepository;
import com.ecommerce.repository.EntregaRepository;
import com.ecommerce.repository.FormaDePagamentoRepository;
import com.ecommerce.repository.PagamentoRepository;
import com.ecommerce.repository.PedidoRepository;
import com.ecommerce.repository.PixRepository;
import com.ecommerce.repository.ProdutoRepository;
import com.ecommerce.repository.TransportadoraRepository;
import com.ecommerce.repository.UsuarioRepository;
import com.ecommerce.repository.VariacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CheckoutService {

    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger((int) (System.currentTimeMillis() % 800_000_000));

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ContemRepository contemRepository;
    private final VariacaoRepository variacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final CupomRepository cupomRepository;
    private final FormaDePagamentoRepository formaDePagamentoRepository;
    private final PixRepository pixRepository;
    private final BoletoRepository boletoRepository;
    private final CartaoRepository cartaoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final EntregaRepository entregaRepository;
    private final PedidoRepository pedidoRepository;
    private final TransportadoraRepository transportadoraRepository;

    public CheckoutService(
        UsuarioRepository usuarioRepository,
        EnderecoRepository enderecoRepository,
        CarrinhoRepository carrinhoRepository,
        ContemRepository contemRepository,
        VariacaoRepository variacaoRepository,
        ProdutoRepository produtoRepository,
        CupomRepository cupomRepository,
        FormaDePagamentoRepository formaDePagamentoRepository,
        PixRepository pixRepository,
        BoletoRepository boletoRepository,
        CartaoRepository cartaoRepository,
        PagamentoRepository pagamentoRepository,
        EntregaRepository entregaRepository,
        PedidoRepository pedidoRepository,
        TransportadoraRepository transportadoraRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.contemRepository = contemRepository;
        this.variacaoRepository = variacaoRepository;
        this.produtoRepository = produtoRepository;
        this.cupomRepository = cupomRepository;
        this.formaDePagamentoRepository = formaDePagamentoRepository;
        this.pixRepository = pixRepository;
        this.boletoRepository = boletoRepository;
        this.cartaoRepository = cartaoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.entregaRepository = entregaRepository;
        this.pedidoRepository = pedidoRepository;
        this.transportadoraRepository = transportadoraRepository;
    }

    @Transactional
    public CheckoutResponse finalizar(CheckoutRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioCpf())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        if (request.itens() == null || request.itens().isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        Endereco endereco = resolveEndereco(request, usuario.cpf());
        Float subtotal = calcularSubtotal(request.itens());
        Cupom cupom = resolveCupom(request.cupomCodigo());
        Float desconto = cupom == null ? 0f : cupom.valor();
        Float total = Math.max(0f, subtotal - desconto);

        Integer carrinhoId = novoId();
        Carrinho carrinho = new Carrinho(carrinhoId, "Carrinho de " + usuario.username(), String.format("%.2f", total), usuario.cpf());
        carrinhoRepository.save(carrinho);

        for (CheckoutItemRequest item : request.itens()) {
            Variacao variacao = variacaoRepository.findById(item.variacaoId())
                .orElseThrow(() -> new RuntimeException("Variação não encontrada: " + item.variacaoId()));
            if (variacao.estoque() < item.quantidade()) {
                throw new RuntimeException("Estoque insuficiente para " + variacao.nome());
            }
            contemRepository.save(new Contem(item.variacaoId(), carrinhoId, item.quantidade()));
            variacaoRepository.updateEstoque(variacao.id(), variacao.estoque() - item.quantidade());
        }

        Integer formaId = novoId();
        formaDePagamentoRepository.save(new FormaDePagamento(formaId, 0, endereco.id()));
        salvarFormaEspecifica(formaId, request.pagamento());

        Integer pagamentoId = novoId();
        pagamentoRepository.save(new Pagamento(pagamentoId, total, LocalDateTime.now(), formaId));

        Transportadora transportadora = transportadoraRepository.ensureDefault();
        Integer entregaId = novoId();
        entregaRepository.save(new Entrega(entregaId, novoId(), LocalDateTime.now().plusDays(7), "Preparando envio", transportadora.id(), endereco.id()));

        String cupomCodigo = cupom == null ? criarCupomSemDesconto() : cupom.codigo();
        if (cupom != null) {
            cupomRepository.update(new Cupom(cupom.codigo(), cupom.valor(), cupom.quantidade() - 1, cupom.validade()));
        }

        Integer pedidoCodigo = novoId();
        pedidoRepository.save(new Pedido(pedidoCodigo, cupomCodigo, carrinhoId, pagamentoId, entregaId));
        return new CheckoutResponse(pedidoCodigo);
    }

    public PedidoDetalheResponse detalharPedido(Integer codigo) {
        Pedido pedido = pedidoRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        Carrinho carrinho = carrinhoRepository.findById(pedido.carrinhoId())
            .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        Usuario usuario = usuarioRepository.findById(carrinho.usuarioCpf())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Pagamento pagamento = pagamentoRepository.findById(pedido.pagamentoId())
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        Entrega entrega = entregaRepository.findById(pedido.entregaId())
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));
        Endereco endereco = enderecoRepository.findById(entrega.enderecoId())
            .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        Cupom cupom = cupomRepository.findById(pedido.cupomCodigo()).orElse(null);

        List<PedidoItemDetalhe> itens = new ArrayList<>();
        Float subtotal = 0f;
        for (Contem contem : contemRepository.findByCarrinhoId(carrinho.id())) {
            Variacao variacao = variacaoRepository.findById(contem.variacaoId())
                .orElseThrow(() -> new RuntimeException("Variação não encontrada"));
            Produto produto = produtoRepository.findById(variacao.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            Float itemSubtotal = variacao.preco() * contem.quantidade();
            subtotal += itemSubtotal;
            itens.add(new PedidoItemDetalhe(produto, variacao, contem.quantidade(), itemSubtotal));
        }
        Float desconto = cupom == null ? 0f : cupom.valor();
        return new PedidoDetalheResponse(pedido, usuario, carrinho, endereco, entrega, pagamento, cupom, itens, subtotal, desconto, Math.max(0f, subtotal - desconto));
    }

    private Endereco resolveEndereco(CheckoutRequest request, Long usuarioCpf) {
        if (request.enderecoId() != null) {
            return enderecoRepository.findById(request.enderecoId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        }
        if (request.novoEndereco() == null) {
            throw new RuntimeException("Informe um endereço");
        }
        Endereco novo = request.novoEndereco();
        Endereco endereco = new Endereco(novo.id() == null || novo.id() == 0 ? novoId() : novo.id(), novo.logradouro(), novo.numero(), novo.bairro(), novo.cep(), novo.nome(), usuarioCpf);
        enderecoRepository.save(endereco);
        return endereco;
    }

    private Float calcularSubtotal(List<CheckoutItemRequest> itens) {
        float subtotal = 0f;
        for (CheckoutItemRequest item : itens) {
            Variacao variacao = variacaoRepository.findById(item.variacaoId())
                .orElseThrow(() -> new RuntimeException("Variação não encontrada: " + item.variacaoId()));
            subtotal += variacao.preco() * item.quantidade();
        }
        return subtotal;
    }

    private Cupom resolveCupom(String cupomCodigo) {
        if (cupomCodigo == null || cupomCodigo.isBlank()) {
            return null;
        }
        Cupom cupom = cupomRepository.findById(cupomCodigo)
            .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));
        if (cupom.quantidade() <= 0 || cupom.validade().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cupom inativo");
        }
        return cupom;
    }

    private void salvarFormaEspecifica(Integer formaId, PagamentoCheckoutRequest pagamento) {
        String tipo = pagamento == null || pagamento.tipo() == null ? "pix" : pagamento.tipo().toLowerCase();
        if ("boleto".equals(tipo)) {
            boletoRepository.save(new Boleto(novoId(), formaId));
            return;
        }
        if ("cartao".equals(tipo) || "cartão".equals(tipo)) {
            cartaoRepository.save(new Cartao(
                pagamento.numero() == null ? novoId() : pagamento.numero(),
                formaId,
                pagamento.titular() == null ? "Cliente Marcel" : pagamento.titular(),
                LocalDateTime.now().plusYears(3),
                pagamento.cvv() == null ? 123 : pagamento.cvv(),
                pagamento.bandeira() == null ? "MarcelCard" : pagamento.bandeira(),
                pagamento.numeroParcelas() == null ? 1 : pagamento.numeroParcelas()
            ));
            return;
        }
        pixRepository.save(new Pix(novoId(), formaId));
    }

    private String criarCupomSemDesconto() {
        String codigo = "SEM-DESCONTO-" + novoId();
        cupomRepository.save(new Cupom(codigo, 0f, 1, LocalDateTime.now().plusDays(1)));
        return codigo;
    }

    private Integer novoId() {
        return ID_SEQUENCE.updateAndGet(current -> current >= 999_999_000 ? 100_000 : current + 1);
    }
}
