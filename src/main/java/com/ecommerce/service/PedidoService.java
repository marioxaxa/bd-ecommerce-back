package com.ecommerce.service;

import com.ecommerce.domain.cupom.Cupom;
import com.ecommerce.domain.entrega.Entrega;
import com.ecommerce.domain.pagamento.Pagamento;
import com.ecommerce.domain.pedido.Pedido;
import com.ecommerce.repository.CupomRepository;
import com.ecommerce.repository.EntregaRepository;
import com.ecommerce.repository.PagamentoRepository;
import com.ecommerce.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CupomRepository cupomRepository;
    private final PagamentoRepository pagamentoRepository;
    private final EntregaRepository entregaRepository;

    public PedidoService(PedidoRepository pedidoRepository, CupomRepository cupomRepository, PagamentoRepository pagamentoRepository, EntregaRepository entregaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.cupomRepository = cupomRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.entregaRepository = entregaRepository;
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Integer codigo) {
        return pedidoRepository.findById(codigo);
    }

    public Pedido save(Pedido pedido) {
        cupomRepository.findById(pedido.cupomCodigo())
            .orElseThrow(() -> new RuntimeException("Cupom não encontrado com código: " + pedido.cupomCodigo()));
        pagamentoRepository.findById(pedido.pagamentoId())
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + pedido.pagamentoId()));
        entregaRepository.findById(pedido.entregaId())
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada com ID: " + pedido.entregaId()));
        
        validarCupom(pedido.cupomCodigo());
        
        pedidoRepository.save(pedido);
        return pedido;
    }

    public Pedido update(Integer codigo, Pedido pedido) {
        pedidoRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com código: " + codigo));
        cupomRepository.findById(pedido.cupomCodigo())
            .orElseThrow(() -> new RuntimeException("Cupom não encontrado com código: " + pedido.cupomCodigo()));
        pagamentoRepository.findById(pedido.pagamentoId())
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + pedido.pagamentoId()));
        entregaRepository.findById(pedido.entregaId())
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada com ID: " + pedido.entregaId()));
        
        pedidoRepository.update(new Pedido(codigo, pedido.cupomCodigo(), pedido.carrinhoId(), pedido.pagamentoId(), pedido.entregaId()));
        return new Pedido(codigo, pedido.cupomCodigo(), pedido.carrinhoId(), pedido.pagamentoId(), pedido.entregaId());
    }

    public void delete(Integer codigo) {
        pedidoRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com código: " + codigo));
        pedidoRepository.delete(codigo);
    }

    private void validarCupom(Integer cupomCodigo) {
        Optional<Cupom> cupom = cupomRepository.findById(cupomCodigo);
        if (cupom.isPresent()) {
            if (cupom.get().quantidade() <= 0) {
                throw new RuntimeException("Cupom esgotado");
            }
            if (cupom.get().validade().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Cupom expirado");
            }
        }
    }

    public List<Cupom> findAllCupons() {
        return cupomRepository.findAll();
    }

    public Optional<Cupom> findCupomById(Integer codigo) {
        return cupomRepository.findById(codigo);
    }

    public Cupom saveCupom(Cupom cupom) {
        cupomRepository.save(cupom);
        return cupom;
    }

    public Cupom updateCupom(Integer codigo, Cupom cupom) {
        cupomRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("Cupom não encontrado com código: " + codigo));
        cupomRepository.update(new Cupom(codigo, cupom.valor(), cupom.quantidade(), cupom.validade()));
        return new Cupom(codigo, cupom.valor(), cupom.quantidade(), cupom.validade());
    }

    public void deleteCupom(Integer codigo) {
        cupomRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("Cupom não encontrado com código: " + codigo));
        cupomRepository.delete(codigo);
    }

    public List<Pagamento> findAllPagamentos() {
        return pagamentoRepository.findAll();
    }

    public Optional<Pagamento> findPagamentoById(Integer id) {
        return pagamentoRepository.findById(id);
    }

    public Pagamento savePagamento(Pagamento pagamento) {
        pagamentoRepository.save(pagamento);
        return pagamento;
    }

    public Pagamento updatePagamento(Integer id, Pagamento pagamento) {
        pagamentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + id));
        pagamentoRepository.update(new Pagamento(id, pagamento.valorTotal(), pagamento.data(), pagamento.formaDePagamentoId()));
        return new Pagamento(id, pagamento.valorTotal(), pagamento.data(), pagamento.formaDePagamentoId());
    }

    public void deletePagamento(Integer id) {
        pagamentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + id));
        pagamentoRepository.delete(id);
    }

    public List<Entrega> findAllEntregas() {
        return entregaRepository.findAll();
    }

    public Optional<Entrega> findEntregaById(Integer id) {
        return entregaRepository.findById(id);
    }

    public Entrega saveEntrega(Entrega entrega) {
        entregaRepository.save(entrega);
        return entrega;
    }

    public Entrega updateEntrega(Integer id, Entrega entrega) {
        entregaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada com ID: " + id));
        entregaRepository.update(new Entrega(id, entrega.codigoRastreio(), entrega.previsao(), entrega.status(), entrega.transportadoraId(), entrega.enderecoId()));
        return new Entrega(id, entrega.codigoRastreio(), entrega.previsao(), entrega.status(), entrega.transportadoraId(), entrega.enderecoId());
    }

    public void deleteEntrega(Integer id) {
        entregaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada com ID: " + id));
        entregaRepository.delete(id);
    }
}
