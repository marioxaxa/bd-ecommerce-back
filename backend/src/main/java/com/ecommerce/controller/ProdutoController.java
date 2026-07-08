package com.ecommerce.controller;

import com.ecommerce.domain.categoria.Categoria;
import com.ecommerce.domain.produto.Produto;
import com.ecommerce.domain.variacao.Variacao;
import com.ecommerce.dto.produto.ProdutoCompletoRequest;
import com.ecommerce.dto.produto.ProdutoDetalheResponse;
import com.ecommerce.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<Produto> findAllProdutos() {
        return produtoService.findAllProdutos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> findProdutoById(@PathVariable Integer id) {
        return produtoService.findProdutoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{cpf}")
    public List<Produto> findProdutosByUsuario(@PathVariable Long cpf) {
        return produtoService.findProdutosByUsuario(cpf);
    }

    @GetMapping("/{id}/detalhe")
    public ResponseEntity<ProdutoDetalheResponse> findProdutoDetalheById(@PathVariable Integer id) {
        return ResponseEntity.ok(produtoService.findProdutoDetalheById(id));
    }

    @GetMapping("/categorias/{categoriaId}/produtos")
    public List<Produto> findProdutosByCategoria(@PathVariable Integer categoriaId) {
        return produtoService.findProdutosByCategoria(categoriaId);
    }

    @PostMapping
    public ResponseEntity<Produto> saveProduto(@RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.saveProduto(produto));
    }

    @PostMapping("/completo")
    public ResponseEntity<ProdutoDetalheResponse> saveProdutoCompleto(@RequestBody ProdutoCompletoRequest request) {
        return ResponseEntity.ok(produtoService.saveProdutoCompleto(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Integer id, @RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.updateProduto(id, produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Integer id) {
        produtoService.deleteProduto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorias")
    public List<Categoria> findAllCategorias() {
        return produtoService.findAllCategorias();
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> findCategoriaById(@PathVariable Integer id) {
        return produtoService.findCategoriaById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> saveCategoria(@RequestBody Categoria categoria) {
        return ResponseEntity.ok(produtoService.saveCategoria(categoria));
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        return ResponseEntity.ok(produtoService.updateCategoria(id, categoria));
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        produtoService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{produtoId}/variacoes")
    public List<Variacao> findVariacoesByProduto(@PathVariable Integer produtoId) {
        return produtoService.findVariacoesByProduto(produtoId);
    }

    @PostMapping("/variacoes")
    public ResponseEntity<Variacao> saveVariacao(@RequestBody Variacao variacao) {
        return ResponseEntity.ok(produtoService.saveVariacao(variacao));
    }

    @PutMapping("/variacoes/{id}")
    public ResponseEntity<Variacao> updateVariacao(@PathVariable Integer id, @RequestBody Variacao variacao) {
        return ResponseEntity.ok(produtoService.updateVariacao(id, variacao));
    }

    @DeleteMapping("/variacoes/{id}")
    public ResponseEntity<Void> deleteVariacao(@PathVariable Integer id) {
        produtoService.deleteVariacao(id);
        return ResponseEntity.noContent().build();
    }
}
