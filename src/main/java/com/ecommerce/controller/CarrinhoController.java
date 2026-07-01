package com.ecommerce.controller;

import com.ecommerce.domain.carrinho.Carrinho;
import com.ecommerce.domain.carrinho.Contem;
import com.ecommerce.service.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrinhos")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping
    public List<Carrinho> findAll() {
        return carrinhoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrinho> findById(@PathVariable Integer id) {
        return carrinhoService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioCpf}")
    public List<Carrinho> findByUsuario(@PathVariable Integer usuarioCpf) {
        return carrinhoService.findByUsuario(usuarioCpf);
    }

    @PostMapping
    public ResponseEntity<Carrinho> save(@RequestBody Carrinho carrinho) {
        return ResponseEntity.ok(carrinhoService.save(carrinho));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carrinho> update(@PathVariable Integer id, @RequestBody Carrinho carrinho) {
        return ResponseEntity.ok(carrinhoService.update(id, carrinho));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        carrinhoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{carrinhoId}/itens")
    public List<Contem> findItensByCarrinho(@PathVariable Integer carrinhoId) {
        return carrinhoService.findItensByCarrinho(carrinhoId);
    }

    @PostMapping("/{carrinhoId}/itens")
    public ResponseEntity<Contem> addItem(@PathVariable Integer carrinhoId, @RequestBody Contem contem) {
        Contem novoItem = new Contem(contem.variacaoId(), carrinhoId, contem.quantidade());
        return ResponseEntity.ok(carrinhoService.addItem(novoItem));
    }

    @PutMapping("/{carrinhoId}/itens/{variacaoId}")
    public ResponseEntity<Contem> updateItem(
        @PathVariable Integer carrinhoId,
        @PathVariable Integer variacaoId,
        @RequestParam Integer quantidade
    ) {
        return ResponseEntity.ok(carrinhoService.updateItem(variacaoId, carrinhoId, quantidade));
    }

    @DeleteMapping("/{carrinhoId}/itens/{variacaoId}")
    public ResponseEntity<Void> removeItem(@PathVariable Integer carrinhoId, @PathVariable Integer variacaoId) {
        carrinhoService.removeItem(variacaoId, carrinhoId);
        return ResponseEntity.noContent().build();
    }
}
