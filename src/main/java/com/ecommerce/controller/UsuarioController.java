package com.ecommerce.controller;

import com.ecommerce.domain.usuario.Usuario;
import com.ecommerce.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Usuario> findById(@PathVariable Integer cpf) {
        return usuarioService.findById(cpf)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.save(usuario));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Usuario> update(@PathVariable Integer cpf, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.update(cpf, usuario));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable Integer cpf) {
        usuarioService.delete(cpf);
        return ResponseEntity.noContent().build();
    }
}
