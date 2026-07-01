package com.ecommerce.service;

import com.ecommerce.domain.usuario.Usuario;
import com.ecommerce.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Integer cpf) {
        return usuarioRepository.findById(cpf);
    }

    public Usuario save(Usuario usuario) {
        usuarioRepository.save(usuario);
        return usuario;
    }

    public Usuario update(Integer cpf, Usuario usuario) {
        usuarioRepository.findById(cpf)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com CPF: " + cpf));
        usuarioRepository.update(new Usuario(cpf, usuario.email(), usuario.senha(), usuario.username(), usuario.gerente()));
        return new Usuario(cpf, usuario.email(), usuario.senha(), usuario.username(), usuario.gerente());
    }

    public void delete(Integer cpf) {
        usuarioRepository.findById(cpf)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com CPF: " + cpf));
        usuarioRepository.delete(cpf);
    }
}
