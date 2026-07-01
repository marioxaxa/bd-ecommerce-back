package com.ecommerce.repository;

import com.ecommerce.domain.cupom.Cupom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CupomRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT codigo, valor, quantidade, validade FROM cupom";
    private static final String SELECT_BY_ID = "SELECT codigo, valor, quantidade, validade FROM cupom WHERE codigo = ?";
    private static final String INSERT = "INSERT INTO cupom (codigo, valor, quantidade, validade) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE cupom SET valor = ?, quantidade = ?, validade = ? WHERE codigo = ?";
    private static final String DELETE = "DELETE FROM cupom WHERE codigo = ?";

    public CupomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cupom> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new CupomRowMapper());
    }

    public Optional<Cupom> findById(Integer codigo) {
        List<Cupom> cupons = jdbcTemplate.query(SELECT_BY_ID, new CupomRowMapper(), codigo);
        return cupons.stream().findFirst();
    }

    public int save(Cupom cupom) {
        return jdbcTemplate.update(INSERT,
            cupom.codigo(),
            cupom.valor(),
            cupom.quantidade(),
            cupom.validade()
        );
    }

    public int update(Cupom cupom) {
        return jdbcTemplate.update(UPDATE,
            cupom.valor(),
            cupom.quantidade(),
            cupom.validade(),
            cupom.codigo()
        );
    }

    public int delete(Integer codigo) {
        return jdbcTemplate.update(DELETE, codigo);
    }

    private static class CupomRowMapper implements RowMapper<Cupom> {
        @Override
        public Cupom mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Cupom(
                rs.getInt("codigo"),
                rs.getFloat("valor"),
                rs.getInt("quantidade"),
                rs.getTimestamp("validade").toLocalDateTime()
            );
        }
    }
}
