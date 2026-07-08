package com.ecommerce.service;

import com.ecommerce.domain.usuario.Usuario;
import com.ecommerce.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long cpf) {
        return usuarioRepository.findById(cpf);
    }

    public Usuario save(Usuario usuario) {
        Usuario usuarioComSenhaCriptografada = new Usuario(usuario.cpf(), usuario.email(), criptografarSenha(usuario.senha()), usuario.username(), usuario.gerente());
        usuarioRepository.save(usuarioComSenhaCriptografada);
        return usuarioComSenhaCriptografada;
    }

    public Usuario login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Email ou senha inválidos."));
        if (!senhaConfere(senha, usuario.senha())) {
            throw new RuntimeException("Email ou senha inválidos.");
        }
        return usuario;
    }

    public Usuario update(Long cpf, Usuario usuario) {
        Usuario usuarioAtual = usuarioRepository.findById(cpf)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com CPF: " + cpf));
        String senha = usuario.senha() == null || usuario.senha().isBlank() ? usuarioAtual.senha() : criptografarSenha(usuario.senha());
        usuarioRepository.update(new Usuario(cpf, usuario.email(), senha, usuario.username(), usuario.gerente()));
        return new Usuario(cpf, usuario.email(), senha, usuario.username(), usuario.gerente());
    }

    public void delete(Long cpf) {
        usuarioRepository.findById(cpf)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com CPF: " + cpf));
        usuarioRepository.delete(cpf);
    }

    private String criptografarSenha(String senha) {
        return senha != null && senha.startsWith("$2") ? senha : passwordEncoder.encode(senha);
    }

    private boolean senhaConfere(String senhaDigitada, String senhaPersistida) {
        if (senhaPersistida != null && senhaPersistida.startsWith("$2")) {
            return passwordEncoder.matches(senhaDigitada, senhaPersistida);
        }
        return senhaPersistida != null && senhaPersistida.equals(senhaDigitada);
    }
}
