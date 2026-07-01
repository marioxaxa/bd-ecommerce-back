package com.ecommerce.service;

import com.ecommerce.domain.categoria.Categoria;
import com.ecommerce.domain.produto.Produto;
import com.ecommerce.domain.variacao.Variacao;
import com.ecommerce.repository.CategoriaRepository;
import com.ecommerce.repository.ProdutoRepository;
import com.ecommerce.repository.VariacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final VariacaoRepository variacaoRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, VariacaoRepository variacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.variacaoRepository = variacaoRepository;
    }

    public List<Produto> findAllProdutos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> findProdutoById(Integer id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> findProdutosByCategoria(Integer categoriaId) {
        categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + categoriaId));
        return produtoRepository.findByCategoriaId(categoriaId);
    }

    public Produto saveProduto(Produto produto) {
        categoriaRepository.findById(produto.categoriaId())
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + produto.categoriaId()));
        produtoRepository.save(produto);
        return produto;
    }

    public Produto updateProduto(Integer id, Produto produto) {
        produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        categoriaRepository.findById(produto.categoriaId())
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + produto.categoriaId()));
        produtoRepository.update(new Produto(id, produto.marca(), produto.nome(), produto.fabricante(), produto.categoriaId()));
        return new Produto(id, produto.marca(), produto.nome(), produto.fabricante(), produto.categoriaId());
    }

    public void deleteProduto(Integer id) {
        produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        produtoRepository.delete(id);
    }

    public List<Categoria> findAllCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findCategoriaById(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria saveCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);
        return categoria;
    }

    public Categoria updateCategoria(Integer id, Categoria categoria) {
        categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));
        categoriaRepository.update(new Categoria(id, categoria.descricao()));
        return new Categoria(id, categoria.descricao());
    }

    public void deleteCategoria(Integer id) {
        categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));
        categoriaRepository.delete(id);
    }

    public List<Variacao> findVariacoesByProduto(Integer produtoId) {
        produtoRepository.findById(produtoId)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + produtoId));
        return variacaoRepository.findByProdutoId(produtoId);
    }

    public Variacao saveVariacao(Variacao variacao) {
        produtoRepository.findById(variacao.produtoId())
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + variacao.produtoId()));
        variacaoRepository.save(variacao);
        return variacao;
    }

    public Variacao updateVariacao(Integer id, Variacao variacao) {
        variacaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Variação não encontrada com ID: " + id));
        produtoRepository.findById(variacao.produtoId())
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + variacao.produtoId()));
        variacaoRepository.update(new Variacao(id, variacao.nome(), variacao.preco(), variacao.estoque(), variacao.fotoUrl(), variacao.produtoId()));
        return new Variacao(id, variacao.nome(), variacao.preco(), variacao.estoque(), variacao.fotoUrl(), variacao.produtoId());
    }

    public void deleteVariacao(Integer id) {
        variacaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Variação não encontrada com ID: " + id));
        variacaoRepository.delete(id);
    }
}
