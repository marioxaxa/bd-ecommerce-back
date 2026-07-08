package com.ecommerce.controller;

import com.ecommerce.domain.cupom.Cupom;
import com.ecommerce.domain.entrega.Entrega;
import com.ecommerce.domain.pagamento.Pagamento;
import com.ecommerce.domain.pedido.Pedido;
import com.ecommerce.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> findAllPedidos() {
        return pedidoService.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Pedido> findPedidoById(@PathVariable Integer codigo) {
        return pedidoService.findById(codigo)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{cpf}")
    public List<Pedido> findPedidosByUsuario(@PathVariable Long cpf) {
        return pedidoService.findByUsuarioCpf(cpf);
    }

    @PostMapping
    public ResponseEntity<Pedido> savePedido(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.save(pedido));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Integer codigo, @RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.update(codigo, pedido));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletePedido(@PathVariable Integer codigo) {
        pedidoService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cupons")
    public List<Cupom> findAllCupons() {
        return pedidoService.findAllCupons();
    }

    @GetMapping("/cupons/{codigo}")
    public ResponseEntity<Cupom> findCupomById(@PathVariable String codigo) {
        return pedidoService.findCupomById(codigo)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cupons")
    public ResponseEntity<Cupom> saveCupom(@RequestBody Cupom cupom) {
        return ResponseEntity.ok(pedidoService.saveCupom(cupom));
    }

    @PutMapping("/cupons/{codigo}")
    public ResponseEntity<Cupom> updateCupom(@PathVariable String codigo, @RequestBody Cupom cupom) {
        return ResponseEntity.ok(pedidoService.updateCupom(codigo, cupom));
    }

    @DeleteMapping("/cupons/{codigo}")
    public ResponseEntity<Void> deleteCupom(@PathVariable String codigo) {
        pedidoService.deleteCupom(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pagamentos")
    public List<Pagamento> findAllPagamentos() {
        return pedidoService.findAllPagamentos();
    }

    @GetMapping("/pagamentos/{id}")
    public ResponseEntity<Pagamento> findPagamentoById(@PathVariable Integer id) {
        return pedidoService.findPagamentoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/pagamentos")
    public ResponseEntity<Pagamento> savePagamento(@RequestBody Pagamento pagamento) {
        return ResponseEntity.ok(pedidoService.savePagamento(pagamento));
    }

    @PutMapping("/pagamentos/{id}")
    public ResponseEntity<Pagamento> updatePagamento(@PathVariable Integer id, @RequestBody Pagamento pagamento) {
        return ResponseEntity.ok(pedidoService.updatePagamento(id, pagamento));
    }

    @DeleteMapping("/pagamentos/{id}")
    public ResponseEntity<Void> deletePagamento(@PathVariable Integer id) {
        pedidoService.deletePagamento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/entregas")
    public List<Entrega> findAllEntregas() {
        return pedidoService.findAllEntregas();
    }

    @GetMapping("/entregas/{id}")
    public ResponseEntity<Entrega> findEntregaById(@PathVariable Integer id) {
        return pedidoService.findEntregaById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/entregas")
    public ResponseEntity<Entrega> saveEntrega(@RequestBody Entrega entrega) {
        return ResponseEntity.ok(pedidoService.saveEntrega(entrega));
    }

    @PutMapping("/entregas/{id}")
    public ResponseEntity<Entrega> updateEntrega(@PathVariable Integer id, @RequestBody Entrega entrega) {
        return ResponseEntity.ok(pedidoService.updateEntrega(id, entrega));
    }

    @DeleteMapping("/entregas/{id}")
    public ResponseEntity<Void> deleteEntrega(@PathVariable Integer id) {
        pedidoService.deleteEntrega(id);
        return ResponseEntity.noContent().build();
    }
}
