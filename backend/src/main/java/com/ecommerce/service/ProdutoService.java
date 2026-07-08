package com.ecommerce.service;

import com.ecommerce.domain.categoria.Categoria;
import com.ecommerce.domain.atributo.Atributo;
import com.ecommerce.domain.produto.Especifica;
import com.ecommerce.domain.produto.Produto;
import com.ecommerce.domain.variacao.Variacao;
import com.ecommerce.dto.produto.ProdutoCompletoRequest;
import com.ecommerce.dto.produto.ProdutoDetalheResponse;
import com.ecommerce.repository.AtributoRepository;
import com.ecommerce.repository.CategoriaRepository;
import com.ecommerce.repository.EspecificaRepository;
import com.ecommerce.repository.ProdutoRepository;
import com.ecommerce.repository.VariacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final VariacaoRepository variacaoRepository;
    private final AtributoRepository atributoRepository;
    private final EspecificaRepository especificaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, VariacaoRepository variacaoRepository, AtributoRepository atributoRepository, EspecificaRepository especificaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.variacaoRepository = variacaoRepository;
        this.atributoRepository = atributoRepository;
        this.especificaRepository = especificaRepository;
    }

    public List<Produto> findAllProdutos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> findProdutoById(Integer id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> findProdutosByUsuario(Long usuarioCpf) {
        return produtoRepository.findByUsuarioCpf(usuarioCpf);
    }

    public ProdutoDetalheResponse findProdutoDetalheById(Integer id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        Categoria categoria = categoriaRepository.findById(produto.categoriaId())
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + produto.categoriaId()));
        return new ProdutoDetalheResponse(
            produto,
            categoria,
            atributoRepository.findByProdutoId(id),
            variacaoRepository.findByProdutoId(id)
        );
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

    @Transactional
    public ProdutoDetalheResponse saveProdutoCompleto(ProdutoCompletoRequest request) {
        Produto produtoRequest = request.produto();
        Categoria categoriaRequest = request.categoria();
        Integer categoriaId = produtoRequest.categoriaId();

        if (categoriaRequest != null) {
            categoriaId = categoriaRequest.id() == null || categoriaRequest.id() == 0 ? categoriaRepository.nextId() : categoriaRequest.id();
            if (categoriaRepository.findById(categoriaId).isEmpty()) {
                categoriaRepository.save(new Categoria(categoriaId, categoriaRequest.descricao()));
            }
        }

        final Integer resolvedCategoriaId = categoriaId;
        categoriaRepository.findById(resolvedCategoriaId)
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + resolvedCategoriaId));

        Integer produtoId = produtoRequest.id() == null || produtoRequest.id() == 0 ? produtoRepository.nextId() : produtoRequest.id();
        Produto produto = new Produto(produtoId, produtoRequest.marca(), produtoRequest.nome(), produtoRequest.fabricante(), resolvedCategoriaId, produtoRequest.descricao(), produtoRequest.usuarioCpf());
        produtoRepository.save(produto);

        if (request.atributos() != null) {
            for (Atributo atributoRequest : request.atributos()) {
                Integer atributoId = atributoRequest.id() == null || atributoRequest.id() == 0 ? atributoRepository.nextId() : atributoRequest.id();
                if (atributoRepository.findById(atributoId).isEmpty()) {
                    atributoRepository.save(new Atributo(atributoId, atributoRequest.valor(), atributoRequest.key()));
                }
                especificaRepository.save(new Especifica(produtoId, atributoId));
            }
        }

        if (request.variacoes() != null) {
            for (Variacao variacaoRequest : request.variacoes()) {
                Integer variacaoId = variacaoRequest.id() == null || variacaoRequest.id() == 0 ? variacaoRepository.nextId() : variacaoRequest.id();
                variacaoRepository.save(new Variacao(variacaoId, variacaoRequest.nome(), variacaoRequest.preco(), variacaoRequest.estoque(), variacaoRequest.fotoUrl(), produtoId));
            }
        }

        return findProdutoDetalheById(produtoId);
    }

    public Produto updateProduto(Integer id, Produto produto) {
        produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        categoriaRepository.findById(produto.categoriaId())
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + produto.categoriaId()));
        produtoRepository.update(new Produto(id, produto.marca(), produto.nome(), produto.fabricante(), produto.categoriaId(), produto.descricao(), produto.usuarioCpf()));
        return new Produto(id, produto.marca(), produto.nome(), produto.fabricante(), produto.categoriaId(), produto.descricao(), produto.usuarioCpf());
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
