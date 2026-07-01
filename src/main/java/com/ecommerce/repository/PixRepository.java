package com.ecommerce.repository;

import com.ecommerce.domain.pagamento.Pix;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PixRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT codigo_pix, Forma_de_pagamento_id FROM pix";
    private static final String SELECT_BY_ID = "SELECT codigo_pix, Forma_de_pagamento_id FROM pix WHERE Forma_de_pagamento_id = ?";
    private static final String INSERT = "INSERT INTO pix (codigo_pix, Forma_de_pagamento_id) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE pix SET codigo_pix = ? WHERE Forma_de_pagamento_id = ?";
    private static final String DELETE = "DELETE FROM pix WHERE Forma_de_pagamento_id = ?";

    public PixRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Pix> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new PixRowMapper());
    }

    public Optional<Pix> findByFormaDePagamentoId(Integer formaDePagamentoId) {
        List<Pix> pixList = jdbcTemplate.query(SELECT_BY_ID, new PixRowMapper(), formaDePagamentoId);
        return pixList.stream().findFirst();
    }

    public int save(Pix pix) {
        return jdbcTemplate.update(INSERT,
            pix.codigoPix(),
            pix.formaDePagamentoId()
        );
    }

    public int update(Pix pix) {
        return jdbcTemplate.update(UPDATE,
            pix.codigoPix(),
            pix.formaDePagamentoId()
        );
    }

    public int delete(Integer formaDePagamentoId) {
        return jdbcTemplate.update(DELETE, formaDePagamentoId);
    }

    private static class PixRowMapper implements RowMapper<Pix> {
        @Override
        public Pix mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Pix(
                rs.getInt("codigo_pix"),
                rs.getInt("Forma_de_pagamento_id")
            );
        }
    }
}
