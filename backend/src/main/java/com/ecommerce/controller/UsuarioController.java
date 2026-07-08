package com.ecommerce.controller;

import com.ecommerce.domain.usuario.Usuario;
import com.ecommerce.dto.usuario.LoginRequest;
import com.ecommerce.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
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
    public ResponseEntity<Usuario> findById(@PathVariable Long cpf) {
        return usuarioService.findById(cpf)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.save(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request.email(), request.senha()));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Usuario> update(@PathVariable Long cpf, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.update(cpf, usuario));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable Long cpf) {
        usuarioService.delete(cpf);
        return ResponseEntity.noContent().build();
    }
}
