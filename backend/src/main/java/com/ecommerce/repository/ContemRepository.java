package com.ecommerce.repository;

import com.ecommerce.domain.carrinho.Contem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ContemRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_CARRINHO = "SELECT Variacao_id, carrinho_id, quanatidade FROM contem WHERE carrinho_id = ?";
    private static final String SELECT_BY_VARIACAO = "SELECT Variacao_id, carrinho_id, quanatidade FROM contem WHERE Variacao_id = ?";
    private static final String INSERT = "INSERT INTO contem (Variacao_id, carrinho_id, quanatidade) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE contem SET quanatidade = ? WHERE Variacao_id = ? AND carrinho_id = ?";
    private static final String DELETE = "DELETE FROM contem WHERE Variacao_id = ? AND carrinho_id = ?";

    public ContemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Contem> findByCarrinhoId(Integer carrinhoId) {
        return jdbcTemplate.query(SELECT_BY_CARRINHO, new ContemRowMapper(), carrinhoId);
    }

    public List<Contem> findByVariacaoId(Integer variacaoId) {
        return jdbcTemplate.query(SELECT_BY_VARIACAO, new ContemRowMapper(), variacaoId);
    }

    public int save(Contem contem) {
        return jdbcTemplate.update(INSERT,
            contem.variacaoId(),
            contem.carrinhoId(),
            contem.quantidade()
        );
    }

    public int update(Contem contem) {
        return jdbcTemplate.update(UPDATE,
            contem.quantidade(),
            contem.variacaoId(),
            contem.carrinhoId()
        );
    }

    public int delete(Integer variacaoId, Integer carrinhoId) {
        return jdbcTemplate.update(DELETE, variacaoId, carrinhoId);
    }

    private static class ContemRowMapper implements RowMapper<Contem> {
        @Override
        public Contem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Contem(
                rs.getInt("Variacao_id"),
                rs.getInt("carrinho_id"),
                rs.getInt("quanatidade")
            );
        }
    }
}
