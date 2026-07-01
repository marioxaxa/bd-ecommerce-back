package com.ecommerce.repository;

import com.ecommerce.domain.transportadora.Transportadora;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TransportadoraRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, nome FROM transportadora";
    private static final String SELECT_BY_ID = "SELECT id, nome FROM transportadora WHERE id = ?";
    private static final String INSERT = "INSERT INTO transportadora (id, nome) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE transportadora SET nome = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM transportadora WHERE id = ?";

    public TransportadoraRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transportadora> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new TransportadoraRowMapper());
    }

    public Optional<Transportadora> findById(Integer id) {
        List<Transportadora> transportadoras = jdbcTemplate.query(SELECT_BY_ID, new TransportadoraRowMapper(), id);
        return transportadoras.stream().findFirst();
    }

    public int save(Transportadora transportadora) {
        return jdbcTemplate.update(INSERT,
            transportadora.id(),
            transportadora.nome()
        );
    }

    public int update(Transportadora transportadora) {
        return jdbcTemplate.update(UPDATE,
            transportadora.nome(),
            transportadora.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class TransportadoraRowMapper implements RowMapper<Transportadora> {
        @Override
        public Transportadora mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Transportadora(
                rs.getInt("id"),
                rs.getString("nome")
            );
        }
    }
}
