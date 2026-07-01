package com.ecommerce.repository;

import com.ecommerce.domain.pagamento.FormaDePagamento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class FormaDePagamentoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, Custo_de_operacao, endereco_id FROM Forma_de_pagamento";
    private static final String SELECT_BY_ID = "SELECT id, Custo_de_operacao, endereco_id FROM Forma_de_pagamento WHERE id = ?";
    private static final String INSERT = "INSERT INTO Forma_de_pagamento (id, Custo_de_operacao, endereco_id) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE Forma_de_pagamento SET Custo_de_operacao = ?, endereco_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Forma_de_pagamento WHERE id = ?";

    public FormaDePagamentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FormaDePagamento> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new FormaDePagamentoRowMapper());
    }

    public Optional<FormaDePagamento> findById(Integer id) {
        List<FormaDePagamento> formas = jdbcTemplate.query(SELECT_BY_ID, new FormaDePagamentoRowMapper(), id);
        return formas.stream().findFirst();
    }

    public int save(FormaDePagamento forma) {
        return jdbcTemplate.update(INSERT,
            forma.id(),
            forma.custoDeOperacao(),
            forma.enderecoId()
        );
    }

    public int update(FormaDePagamento forma) {
        return jdbcTemplate.update(UPDATE,
            forma.custoDeOperacao(),
            forma.enderecoId(),
            forma.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class FormaDePagamentoRowMapper implements RowMapper<FormaDePagamento> {
        @Override
        public FormaDePagamento mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FormaDePagamento(
                rs.getInt("id"),
                rs.getInt("Custo_de_operacao"),
                rs.getInt("endereco_id")
            );
        }
    }
}
