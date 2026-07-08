package com.ecommerce.service;

import com.ecommerce.domain.usuario.Usuario;
import com.ecommerce.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(123456789L, "teste@email.com", "senha123", "testuser", false);
    }

    @Test
    @DisplayName("Deve retornar lista de usuários quando findAll for chamado")
    void deveRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> result = usuarioService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).cpf()).isEqualTo(usuario.cpf());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver usuários")
    void deveRetornarListaVazia() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<Usuario> result = usuarioService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar usuário quando findById encontrar")
    void deveRetornarUsuarioQuandoEncontrar() {
        when(usuarioRepository.findById(usuario.cpf())).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.findById(usuario.cpf());

        assertThat(result).isPresent();
        assertThat(result.get().cpf()).isEqualTo(usuario.cpf());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando findById não encontrar")
    void deveRetornarOptionalVazio() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioService.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso")
    void deveSalvarUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(1);

        Usuario result = usuarioService.save(usuario);

        assertThat(result.cpf()).isEqualTo(usuario.cpf());
        assertThat(result.senha()).startsWith("$2");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve atualizar usuário existente")
    void deveAtualizarUsuario() {
        Usuario usuarioAtualizado = new Usuario(usuario.cpf(), "novo@email.com", "novaSenha", "newuser", true);
        
        when(usuarioRepository.findById(usuario.cpf())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.update(any(Usuario.class))).thenReturn(1);

        Usuario result = usuarioService.update(usuario.cpf(), usuarioAtualizado);

        assertThat(result.cpf()).isEqualTo(usuario.cpf());
        assertThat(result.email()).isEqualTo(usuarioAtualizado.email());
        verify(usuarioRepository, times(1)).findById(usuario.cpf());
        verify(usuarioRepository, times(1)).update(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar usuário inexistente")
    void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.update(999L, usuario))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Usuário não encontrado");
    }

    @Test
    @DisplayName("Deve deletar usuário existente")
    void deveDeletarUsuario() {
        when(usuarioRepository.findById(usuario.cpf())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.delete(usuario.cpf())).thenReturn(1);

        usuarioService.delete(usuario.cpf());

        verify(usuarioRepository, times(1)).findById(usuario.cpf());
        verify(usuarioRepository, times(1)).delete(usuario.cpf());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar usuário inexistente")
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.delete(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Usuário não encontrado");
    }
}
