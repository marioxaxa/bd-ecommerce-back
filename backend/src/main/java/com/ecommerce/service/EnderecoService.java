package com.ecommerce.service;

import com.ecommerce.domain.endereco.Endereco;
import com.ecommerce.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

    public Optional<Endereco> findById(Integer id) {
        return enderecoRepository.findById(id);
    }

    public List<Endereco> findByUsuarioCpf(Long usuarioCpf) {
        return enderecoRepository.findByUsuarioCpf(usuarioCpf);
    }

    public Endereco save(Endereco endereco) {
        enderecoRepository.save(endereco);
        return endereco;
    }
}
