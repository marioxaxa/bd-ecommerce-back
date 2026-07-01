package com.ecommerce.repository;

import com.ecommerce.domain.categoria.Categoria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, descricao FROM categoria";
    private static final String SELECT_BY_ID = "SELECT id, descricao FROM categoria WHERE id = ?";
    private static final String INSERT = "INSERT INTO categoria (id, descricao) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE categoria SET descricao = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM categoria WHERE id = ?";

    public CategoriaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Categoria> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new CategoriaRowMapper());
    }

    public Optional<Categoria> findById(Integer id) {
        List<Categoria> categorias = jdbcTemplate.query(SELECT_BY_ID, new CategoriaRowMapper(), id);
        return categorias.stream().findFirst();
    }

    public int save(Categoria categoria) {
        return jdbcTemplate.update(INSERT, categoria.id(), categoria.descricao());
    }

    public int update(Categoria categoria) {
        return jdbcTemplate.update(UPDATE, categoria.descricao(), categoria.id());
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class CategoriaRowMapper implements RowMapper<Categoria> {
        @Override
        public Categoria mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Categoria(
                rs.getInt("id"),
                rs.getString("descricao")
            );
        }
    }
}
