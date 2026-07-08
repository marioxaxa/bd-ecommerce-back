package com.ecommerce.repository;

import com.ecommerce.domain.atributo.Atributo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AtributoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, valor, `key` FROM atributo";
    private static final String SELECT_BY_ID = "SELECT id, valor, `key` FROM atributo WHERE id = ?";
    private static final String SELECT_BY_PRODUTO = "SELECT a.id, a.valor, a.`key` FROM atributo a INNER JOIN especifica e ON e.atributo_id = a.id WHERE e.Produto_id = ?";
    private static final String INSERT = "INSERT INTO atributo (id, valor, `key`) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE atributo SET valor = ?, `key` = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM atributo WHERE id = ?";

    public AtributoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Atributo> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new AtributoRowMapper());
    }

    public Optional<Atributo> findById(Integer id) {
        List<Atributo> atributos = jdbcTemplate.query(SELECT_BY_ID, new AtributoRowMapper(), id);
        return atributos.stream().findFirst();
    }

    public List<Atributo> findByProdutoId(Integer produtoId) {
        return jdbcTemplate.query(SELECT_BY_PRODUTO, new AtributoRowMapper(), produtoId);
    }

    public int save(Atributo atributo) {
        return jdbcTemplate.update(INSERT,
            atributo.id(),
            atributo.valor(),
            atributo.key()
        );
    }

    public int update(Atributo atributo) {
        return jdbcTemplate.update(UPDATE,
            atributo.valor(),
            atributo.key(),
            atributo.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    public Integer nextId() {
        Integer maxId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(id), 0) FROM atributo", Integer.class);
        return (maxId == null ? 0 : maxId) + 1;
    }

    private static class AtributoRowMapper implements RowMapper<Atributo> {
        @Override
        public Atributo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Atributo(
                rs.getInt("id"),
                rs.getString("valor"),
                rs.getString("key")
            );
        }
    }
}
