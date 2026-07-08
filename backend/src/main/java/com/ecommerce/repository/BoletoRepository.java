package com.ecommerce.repository;

import com.ecommerce.domain.pagamento.Boleto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class BoletoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT codigo_de_barras, Forma_de_pagamento_id FROM boleto";
    private static final String SELECT_BY_ID = "SELECT codigo_de_barras, Forma_de_pagamento_id FROM boleto WHERE Forma_de_pagamento_id = ?";
    private static final String INSERT = "INSERT INTO boleto (codigo_de_barras, Forma_de_pagamento_id) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE boleto SET codigo_de_barras = ? WHERE Forma_de_pagamento_id = ?";
    private static final String DELETE = "DELETE FROM boleto WHERE Forma_de_pagamento_id = ?";

    public BoletoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Boleto> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BoletoRowMapper());
    }

    public Optional<Boleto> findByFormaDePagamentoId(Integer formaDePagamentoId) {
        List<Boleto> boletos = jdbcTemplate.query(SELECT_BY_ID, new BoletoRowMapper(), formaDePagamentoId);
        return boletos.stream().findFirst();
    }

    public int save(Boleto boleto) {
        return jdbcTemplate.update(INSERT,
            boleto.codigoDeBarras(),
            boleto.formaDePagamentoId()
        );
    }

    public int update(Boleto boleto) {
        return jdbcTemplate.update(UPDATE,
            boleto.codigoDeBarras(),
            boleto.formaDePagamentoId()
        );
    }

    public int delete(Integer formaDePagamentoId) {
        return jdbcTemplate.update(DELETE, formaDePagamentoId);
    }

    private static class BoletoRowMapper implements RowMapper<Boleto> {
        @Override
        public Boleto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Boleto(
                rs.getInt("codigo_de_barras"),
                rs.getInt("Forma_de_pagamento_id")
            );
        }
    }
}
