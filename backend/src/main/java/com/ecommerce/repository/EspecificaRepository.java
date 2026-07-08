package com.ecommerce.repository;

import com.ecommerce.domain.produto.Especifica;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EspecificaRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_PRODUTO = "SELECT Produto_id, atributo_id FROM especifica WHERE Produto_id = ?";
    private static final String SELECT_BY_ATRIBUTO = "SELECT Produto_id, atributo_id FROM especifica WHERE atributo_id = ?";
    private static final String INSERT = "INSERT INTO especifica (Produto_id, atributo_id) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM especifica WHERE Produto_id = ? AND atributo_id = ?";

    public EspecificaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Especifica> findByProdutoId(Integer produtoId) {
        return jdbcTemplate.query(SELECT_BY_PRODUTO, new EspecificaRowMapper(), produtoId);
    }

    public List<Especifica> findByAtributoId(Integer atributoId) {
        return jdbcTemplate.query(SELECT_BY_ATRIBUTO, new EspecificaRowMapper(), atributoId);
    }

    public int save(Especifica especifica) {
        return jdbcTemplate.update(INSERT,
            especifica.produtoId(),
            especifica.atributoId()
        );
    }

    public int delete(Integer produtoId, Integer atributoId) {
        return jdbcTemplate.update(DELETE, produtoId, atributoId);
    }

    private static class EspecificaRowMapper implements RowMapper<Especifica> {
        @Override
        public Especifica mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Especifica(
                rs.getInt("Produto_id"),
                rs.getInt("atributo_id")
            );
        }
    }
}
