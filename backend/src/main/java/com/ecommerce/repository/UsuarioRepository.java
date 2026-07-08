package com.ecommerce.repository;

import com.ecommerce.domain.usuario.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT cpf, email, senha, username, gerente FROM usuario";
    private static final String SELECT_BY_ID = "SELECT cpf, email, senha, username, gerente FROM usuario WHERE cpf = ?";
    private static final String SELECT_BY_EMAIL = "SELECT cpf, email, senha, username, gerente FROM usuario WHERE email = ?";
    private static final String INSERT = "INSERT INTO usuario (cpf, email, senha, username, gerente) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE usuario SET email = ?, senha = ?, username = ?, gerente = ? WHERE cpf = ?";
    private static final String DELETE = "DELETE FROM usuario WHERE cpf = ?";

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Usuario> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new UsuarioRowMapper());
    }

    public Optional<Usuario> findById(Long cpf) {
        List<Usuario> usuarios = jdbcTemplate.query(SELECT_BY_ID, new UsuarioRowMapper(), cpf);
        return usuarios.stream().findFirst();
    }

    public Optional<Usuario> findByEmail(String email) {
        List<Usuario> usuarios = jdbcTemplate.query(SELECT_BY_EMAIL, new UsuarioRowMapper(), email);
        return usuarios.stream().findFirst();
    }

    public int save(Usuario usuario) {
        return jdbcTemplate.update(INSERT,
            usuario.cpf(),
            usuario.email(),
            usuario.senha(),
            usuario.username(),
            usuario.gerente()
        );
    }

    public int update(Usuario usuario) {
        return jdbcTemplate.update(UPDATE,
            usuario.email(),
            usuario.senha(),
            usuario.username(),
            usuario.gerente(),
            usuario.cpf()
        );
    }

    public int delete(Long cpf) {
        return jdbcTemplate.update(DELETE, cpf);
    }

    private static class UsuarioRowMapper implements RowMapper<Usuario> {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Usuario(
                rs.getLong("cpf"),
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("username"),
                rs.getBoolean("gerente")
            );
        }
    }
}
