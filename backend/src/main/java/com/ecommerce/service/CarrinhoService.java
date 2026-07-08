package com.ecommerce.service;

import com.ecommerce.domain.carrinho.Carrinho;
import com.ecommerce.domain.carrinho.Contem;
import com.ecommerce.domain.variacao.Variacao;
import com.ecommerce.repository.CarrinhoRepository;
import com.ecommerce.repository.ContemRepository;
import com.ecommerce.repository.VariacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ContemRepository contemRepository;
    private final VariacaoRepository variacaoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository, ContemRepository contemRepository, VariacaoRepository variacaoRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.contemRepository = contemRepository;
        this.variacaoRepository = variacaoRepository;
    }

    public List<Carrinho> findAll() {
        return carrinhoRepository.findAll();
    }

    public Optional<Carrinho> findById(Integer id) {
        return carrinhoRepository.findById(id);
    }

    public List<Carrinho> findByUsuario(Long usuarioCpf) {
        return carrinhoRepository.findByUsuarioCpf(usuarioCpf);
    }

    public Carrinho save(Carrinho carrinho) {
        carrinhoRepository.save(carrinho);
        return carrinho;
    }

    public Carrinho update(Integer id, Carrinho carrinho) {
        carrinhoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Carrinho não encontrado com ID: " + id));
        carrinhoRepository.update(new Carrinho(id, carrinho.nome(), carrinho.valorTotal(), carrinho.usuarioCpf()));
        return new Carrinho(id, carrinho.nome(), carrinho.valorTotal(), carrinho.usuarioCpf());
    }

    public void delete(Integer id) {
        carrinhoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Carrinho não encontrado com ID: " + id));
        carrinhoRepository.delete(id);
    }

    public List<Contem> findItensByCarrinho(Integer carrinhoId) {
        carrinhoRepository.findById(carrinhoId)
            .orElseThrow(() -> new RuntimeException("Carrinho não encontrado com ID: " + carrinhoId));
        return contemRepository.findByCarrinhoId(carrinhoId);
    }

    public Contem addItem(Contem contem) {
        carrinhoRepository.findById(contem.carrinhoId())
            .orElseThrow(() -> new RuntimeException("Carrinho não encontrado com ID: " + contem.carrinhoId()));
        variacaoRepository.findById(contem.variacaoId())
            .orElseThrow(() -> new RuntimeException("Variação não encontrada com ID: " + contem.variacaoId()));
        
        Optional<Variacao> variacao = variacaoRepository.findById(contem.variacaoId());
        if (variacao.isPresent() && variacao.get().estoque() < contem.quantidade()) {
            throw new RuntimeException("Estoque insuficiente para a variação ID: " + contem.variacaoId());
        }
        
        contemRepository.save(contem);
        return contem;
    }

    public Contem updateItem(Integer variacaoId, Integer carrinhoId, Integer quantidade) {
        variacaoRepository.findById(variacaoId)
            .orElseThrow(() -> new RuntimeException("Variação não encontrada com ID: " + variacaoId));
        carrinhoRepository.findById(carrinhoId)
            .orElseThrow(() -> new RuntimeException("Carrinho não encontrado com ID: " + carrinhoId));
        
        Optional<Variacao> variacao = variacaoRepository.findById(variacaoId);
        if (variacao.isPresent() && variacao.get().estoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente para a variação ID: " + variacaoId);
        }
        
        contemRepository.update(new Contem(variacaoId, carrinhoId, quantidade));
        return new Contem(variacaoId, carrinhoId, quantidade);
    }

    public void removeItem(Integer variacaoId, Integer carrinhoId) {
        contemRepository.delete(variacaoId, carrinhoId);
    }
}
