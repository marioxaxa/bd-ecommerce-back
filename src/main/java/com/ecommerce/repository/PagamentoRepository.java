package com.ecommerce.repository;

import com.ecommerce.domain.pagamento.Pagamento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PagamentoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, valor_total, data, Forma_de_pagamento_id FROM pagamento";
    private static final String SELECT_BY_ID = "SELECT id, valor_total, data, Forma_de_pagamento_id FROM pagamento WHERE id = ?";
    private static final String INSERT = "INSERT INTO pagamento (id, valor_total, data, Forma_de_pagamento_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE pagamento SET valor_total = ?, data = ?, Forma_de_pagamento_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM pagamento WHERE id = ?";

    public PagamentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Pagamento> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new PagamentoRowMapper());
    }

    public Optional<Pagamento> findById(Integer id) {
        List<Pagamento> pagamentos = jdbcTemplate.query(SELECT_BY_ID, new PagamentoRowMapper(), id);
        return pagamentos.stream().findFirst();
    }

    public int save(Pagamento pagamento) {
        return jdbcTemplate.update(INSERT,
            pagamento.id(),
            pagamento.valorTotal(),
            pagamento.data(),
            pagamento.formaDePagamentoId()
        );
    }

    public int update(Pagamento pagamento) {
        return jdbcTemplate.update(UPDATE,
            pagamento.valorTotal(),
            pagamento.data(),
            pagamento.formaDePagamentoId(),
            pagamento.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class PagamentoRowMapper implements RowMapper<Pagamento> {
        @Override
        public Pagamento mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Pagamento(
                rs.getInt("id"),
                rs.getFloat("valor_total"),
                rs.getTimestamp("data").toLocalDateTime(),
                rs.getInt("Forma_de_pagamento_id")
            );
        }
    }
}
