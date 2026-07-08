package com.ecommerce.repository;

import com.ecommerce.domain.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(UsuarioRepository.class)
@ActiveProfiles("test")
@Sql(statements = "DELETE FROM usuario")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(123456789L, "teste@email.com", "senha123", "testuser", false);
    }

    @Test
    @DisplayName("Deve salvar e encontrar usuário por ID")
    void deveSalvarEncontrarUsuario() {
        usuarioRepository.save(usuario);

        Optional<Usuario> result = usuarioRepository.findById(usuario.cpf());

        assertThat(result).isPresent();
        assertThat(result.get().cpf()).isEqualTo(usuario.cpf());
        assertThat(result.get().email()).isEqualTo(usuario.email());
        assertThat(result.get().username()).isEqualTo(usuario.username());
        assertThat(result.get().gerente()).isEqualTo(usuario.gerente());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando usuário não existir")
    void deveRetornarVazioQuandoNaoExistir() {
        Optional<Usuario> result = usuarioRepository.findById(999999999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar todos os usuários")
    void deveRetornarTodosUsuarios() {
        Usuario usuario1 = new Usuario(111111111L, "user1@email.com", "senha1", "user1", false);
        Usuario usuario2 = new Usuario(222222222L, "user2@email.com", "senha2", "user2", true);
        
        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);

        List<Usuario> result = usuarioRepository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve atualizar usuário")
    void deveAtualizarUsuario() {
        usuarioRepository.save(usuario);
        
        Usuario usuarioAtualizado = new Usuario(usuario.cpf(), "novo@email.com", "novaSenha", "newuser", true);
        usuarioRepository.update(usuarioAtualizado);

        Optional<Usuario> result = usuarioRepository.findById(usuario.cpf());

        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("novo@email.com");
        assertThat(result.get().username()).isEqualTo("newuser");
        assertThat(result.get().gerente()).isTrue();
    }

    @Test
    @DisplayName("Deve deletar usuário")
    void deveDeletarUsuario() {
        usuarioRepository.save(usuario);
        
        int rowsAffected = usuarioRepository.delete(usuario.cpf());

        assertThat(rowsAffected).isEqualTo(1);
        
        Optional<Usuario> result = usuarioRepository.findById(usuario.cpf());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar 0 ao deletar usuário inexistente")
    void deveRetornarZeroAoDeletarInexistente() {
        int rowsAffected = usuarioRepository.delete(999999999L);

        assertThat(rowsAffected).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve mapear corretamente o campo gerente (TINYINT para Boolean)")
    void deveMapearCampoGerente() {
        Usuario gerente = new Usuario(111111111L, "gerente@email.com", "senha", "gerente", true);
        Usuario cliente = new Usuario(222222222L, "cliente@email.com", "senha", "cliente", false);
        
        usuarioRepository.save(gerente);
        usuarioRepository.save(cliente);

        Optional<Usuario> gerenteResult = usuarioRepository.findById(111111111L);
        Optional<Usuario> clienteResult = usuarioRepository.findById(222222222L);

        assertThat(gerenteResult).isPresent();
        assertThat(gerenteResult.get().gerente()).isTrue();
        
        assertThat(clienteResult).isPresent();
        assertThat(clienteResult.get().gerente()).isFalse();
    }
}
