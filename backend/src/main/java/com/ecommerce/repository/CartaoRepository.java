package com.ecommerce.repository;

import com.ecommerce.domain.pagamento.Cartao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CartaoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT numero, Forma_de_pagamento_id, titular, validade, cvv, bandeira, numero_parcelas FROM cartao";
    private static final String SELECT_BY_ID = "SELECT numero, Forma_de_pagamento_id, titular, validade, cvv, bandeira, numero_parcelas FROM cartao WHERE Forma_de_pagamento_id = ?";
    private static final String INSERT = "INSERT INTO cartao (numero, Forma_de_pagamento_id, titular, validade, cvv, bandeira, numero_parcelas) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE cartao SET numero = ?, titular = ?, validade = ?, cvv = ?, bandeira = ?, numero_parcelas = ? WHERE Forma_de_pagamento_id = ?";
    private static final String DELETE = "DELETE FROM cartao WHERE Forma_de_pagamento_id = ?";

    public CartaoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cartao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new CartaoRowMapper());
    }

    public Optional<Cartao> findByFormaDePagamentoId(Integer formaDePagamentoId) {
        List<Cartao> cartoes = jdbcTemplate.query(SELECT_BY_ID, new CartaoRowMapper(), formaDePagamentoId);
        return cartoes.stream().findFirst();
    }

    public int save(Cartao cartao) {
        return jdbcTemplate.update(INSERT,
            cartao.numero(),
            cartao.formaDePagamentoId(),
            cartao.titular(),
            cartao.validade(),
            cartao.cvv(),
            cartao.bandeira(),
            cartao.numeroParcelas()
        );
    }

    public int update(Cartao cartao) {
        return jdbcTemplate.update(UPDATE,
            cartao.numero(),
            cartao.titular(),
            cartao.validade(),
            cartao.cvv(),
            cartao.bandeira(),
            cartao.numeroParcelas(),
            cartao.formaDePagamentoId()
        );
    }

    public int delete(Integer formaDePagamentoId) {
        return jdbcTemplate.update(DELETE, formaDePagamentoId);
    }

    private static class CartaoRowMapper implements RowMapper<Cartao> {
        @Override
        public Cartao mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Cartao(
                rs.getInt("numero"),
                rs.getInt("Forma_de_pagamento_id"),
                rs.getString("titular"),
                rs.getTimestamp("validade").toLocalDateTime(),
                rs.getInt("cvv"),
                rs.getString("bandeira"),
                rs.getInt("numero_parcelas")
            );
        }
    }
}
