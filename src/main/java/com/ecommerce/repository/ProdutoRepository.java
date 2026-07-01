package com.ecommerce.repository;

import com.ecommerce.domain.produto.Produto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProdutoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, marca, nome, fabricante, categoria_id FROM Produto";
    private static final String SELECT_BY_ID = "SELECT id, marca, nome, fabricante, categoria_id FROM Produto WHERE id = ?";
    private static final String SELECT_BY_CATEGORIA = "SELECT id, marca, nome, fabricante, categoria_id FROM Produto WHERE categoria_id = ?";
    private static final String INSERT = "INSERT INTO Produto (id, marca, nome, fabricante, categoria_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Produto SET marca = ?, nome = ?, fabricante = ?, categoria_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Produto WHERE id = ?";

    public ProdutoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Produto> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new ProdutoRowMapper());
    }

    public Optional<Produto> findById(Integer id) {
        List<Produto> produtos = jdbcTemplate.query(SELECT_BY_ID, new ProdutoRowMapper(), id);
        return produtos.stream().findFirst();
    }

    public List<Produto> findByCategoriaId(Integer categoriaId) {
        return jdbcTemplate.query(SELECT_BY_CATEGORIA, new ProdutoRowMapper(), categoriaId);
    }

    public int save(Produto produto) {
        return jdbcTemplate.update(INSERT,
            produto.id(),
            produto.marca(),
            produto.nome(),
            produto.fabricante(),
            produto.categoriaId()
        );
    }

    public int update(Produto produto) {
        return jdbcTemplate.update(UPDATE,
            produto.marca(),
            produto.nome(),
            produto.fabricante(),
            produto.categoriaId(),
            produto.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class ProdutoRowMapper implements RowMapper<Produto> {
        @Override
        public Produto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Produto(
                rs.getInt("id"),
                rs.getString("marca"),
                rs.getString("nome"),
                rs.getString("fabricante"),
                rs.getInt("categoria_id")
            );
        }
    }
}
