package com.ecommerce.repository;

import com.ecommerce.domain.variacao.TemAtributo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TemAtributoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_VARIACAO = "SELECT Variacao_id, atributo_id FROM tem_atributo WHERE Variacao_id = ?";
    private static final String SELECT_BY_ATRIBUTO = "SELECT Variacao_id, atributo_id FROM tem_atributo WHERE atributo_id = ?";
    private static final String INSERT = "INSERT INTO tem_atributo (Variacao_id, atributo_id) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM tem_atributo WHERE Variacao_id = ? AND atributo_id = ?";

    public TemAtributoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TemAtributo> findByVariacaoId(Integer variacaoId) {
        return jdbcTemplate.query(SELECT_BY_VARIACAO, new TemAtributoRowMapper(), variacaoId);
    }

    public List<TemAtributo> findByAtributoId(Integer atributoId) {
        return jdbcTemplate.query(SELECT_BY_ATRIBUTO, new TemAtributoRowMapper(), atributoId);
    }

    public int save(TemAtributo temAtributo) {
        return jdbcTemplate.update(INSERT,
            temAtributo.variacaoId(),
            temAtributo.atributoId()
        );
    }

    public int delete(Integer variacaoId, Integer atributoId) {
        return jdbcTemplate.update(DELETE, variacaoId, atributoId);
    }

    private static class TemAtributoRowMapper implements RowMapper<TemAtributo> {
        @Override
        public TemAtributo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TemAtributo(
                rs.getInt("Variacao_id"),
                rs.getInt("atributo_id")
            );
        }
    }
}
