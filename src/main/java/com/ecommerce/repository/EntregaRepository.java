package com.ecommerce.repository;

import com.ecommerce.domain.entrega.Entrega;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class EntregaRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, codigo_rastreio, previsao, status, transportadora_id, endereco_id FROM entrega";
    private static final String SELECT_BY_ID = "SELECT id, codigo_rastreio, previsao, status, transportadora_id, endereco_id FROM entrega WHERE id = ?";
    private static final String INSERT = "INSERT INTO entrega (id, codigo_rastreio, previsao, status, transportadora_id, endereco_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE entrega SET codigo_rastreio = ?, previsao = ?, status = ?, transportadora_id = ?, endereco_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM entrega WHERE id = ?";

    public EntregaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Entrega> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new EntregaRowMapper());
    }

    public Optional<Entrega> findById(Integer id) {
        List<Entrega> entregas = jdbcTemplate.query(SELECT_BY_ID, new EntregaRowMapper(), id);
        return entregas.stream().findFirst();
    }

    public int save(Entrega entrega) {
        return jdbcTemplate.update(INSERT,
            entrega.id(),
            entrega.codigoRastreio(),
            entrega.previsao(),
            entrega.status(),
            entrega.transportadoraId(),
            entrega.enderecoId()
        );
    }

    public int update(Entrega entrega) {
        return jdbcTemplate.update(UPDATE,
            entrega.codigoRastreio(),
            entrega.previsao(),
            entrega.status(),
            entrega.transportadoraId(),
            entrega.enderecoId(),
            entrega.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class EntregaRowMapper implements RowMapper<Entrega> {
        @Override
        public Entrega mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Entrega(
                rs.getInt("id"),
                rs.getInt("codigo_rastreio"),
                rs.getTimestamp("previsao").toLocalDateTime(),
                rs.getString("status"),
                rs.getInt("transportadora_id"),
                rs.getInt("endereco_id")
            );
        }
    }
}
