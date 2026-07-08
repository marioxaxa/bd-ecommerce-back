package com.ecommerce.repository;

import com.ecommerce.domain.endereco.Endereco;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class EnderecoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, logradouro, numero, bairro, cep, nome, usuario_cpf FROM endereco";
    private static final String SELECT_BY_ID = "SELECT id, logradouro, numero, bairro, cep, nome, usuario_cpf FROM endereco WHERE id = ?";
    private static final String SELECT_BY_USUARIO = "SELECT id, logradouro, numero, bairro, cep, nome, usuario_cpf FROM endereco WHERE usuario_cpf = ?";
    private static final String INSERT = "INSERT INTO endereco (id, logradouro, numero, bairro, cep, nome, usuario_cpf) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE endereco SET logradouro = ?, numero = ?, bairro = ?, cep = ?, nome = ?, usuario_cpf = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM endereco WHERE id = ?";

    public EnderecoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Endereco> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new EnderecoRowMapper());
    }

    public Optional<Endereco> findById(Integer id) {
        List<Endereco> enderecos = jdbcTemplate.query(SELECT_BY_ID, new EnderecoRowMapper(), id);
        return enderecos.stream().findFirst();
    }

    public List<Endereco> findByUsuarioCpf(Long usuarioCpf) {
        return jdbcTemplate.query(SELECT_BY_USUARIO, new EnderecoRowMapper(), usuarioCpf);
    }

    public int save(Endereco endereco) {
        return jdbcTemplate.update(INSERT,
            endereco.id(),
            endereco.logradouro(),
            endereco.numero(),
            endereco.bairro(),
            endereco.cep(),
            endereco.nome(),
            endereco.usuarioCpf()
        );
    }

    public int update(Endereco endereco) {
        return jdbcTemplate.update(UPDATE,
            endereco.logradouro(),
            endereco.numero(),
            endereco.bairro(),
            endereco.cep(),
            endereco.nome(),
            endereco.usuarioCpf(),
            endereco.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class EnderecoRowMapper implements RowMapper<Endereco> {
        @Override
        public Endereco mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Endereco(
                rs.getInt("id"),
                rs.getString("logradouro"),
                rs.getInt("numero"),
                rs.getString("bairro"),
                rs.getInt("cep"),
                rs.getString("nome"),
                rs.getLong("usuario_cpf")
            );
        }
    }
}
