package com.ecommerce.controller;

import com.ecommerce.domain.endereco.Endereco;
import com.ecommerce.service.EnderecoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public List<Endereco> findAll() {
        return enderecoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> findById(@PathVariable Integer id) {
        return enderecoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioCpf}")
    public List<Endereco> findByUsuarioCpf(@PathVariable Long usuarioCpf) {
        return enderecoService.findByUsuarioCpf(usuarioCpf);
    }

    @PostMapping
    public ResponseEntity<Endereco> save(@RequestBody Endereco endereco) {
        return ResponseEntity.ok(enderecoService.save(endereco));
    }
}
