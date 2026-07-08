package com.ecommerce.repository;

import com.ecommerce.domain.preco.PrecoHistorico;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PrecoHistoricoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, valor, data_inicio, data_fim, Variacao_id FROM Preco_historico";
    private static final String SELECT_BY_ID = "SELECT id, valor, data_inicio, data_fim, Variacao_id FROM Preco_historico WHERE id = ?";
    private static final String SELECT_BY_VARIACAO = "SELECT id, valor, data_inicio, data_fim, Variacao_id FROM Preco_historico WHERE Variacao_id = ?";
    private static final String INSERT = "INSERT INTO Preco_historico (id, valor, data_inicio, data_fim, Variacao_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Preco_historico SET valor = ?, data_inicio = ?, data_fim = ?, Variacao_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Preco_historico WHERE id = ?";

    public PrecoHistoricoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PrecoHistorico> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new PrecoHistoricoRowMapper());
    }

    public Optional<PrecoHistorico> findById(Integer id) {
        List<PrecoHistorico> historicos = jdbcTemplate.query(SELECT_BY_ID, new PrecoHistoricoRowMapper(), id);
        return historicos.stream().findFirst();
    }

    public List<PrecoHistorico> findByVariacaoId(Integer variacaoId) {
        return jdbcTemplate.query(SELECT_BY_VARIACAO, new PrecoHistoricoRowMapper(), variacaoId);
    }

    public int save(PrecoHistorico precoHistorico) {
        return jdbcTemplate.update(INSERT,
            precoHistorico.id(),
            precoHistorico.valor(),
            precoHistorico.dataInicio(),
            precoHistorico.dataFim(),
            precoHistorico.variacaoId()
        );
    }

    public int update(PrecoHistorico precoHistorico) {
        return jdbcTemplate.update(UPDATE,
            precoHistorico.valor(),
            precoHistorico.dataInicio(),
            precoHistorico.dataFim(),
            precoHistorico.variacaoId(),
            precoHistorico.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class PrecoHistoricoRowMapper implements RowMapper<PrecoHistorico> {
        @Override
        public PrecoHistorico mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PrecoHistorico(
                rs.getInt("id"),
                rs.getFloat("valor"),
                rs.getTimestamp("data_inicio").toLocalDateTime(),
                rs.getTimestamp("data_fim").toLocalDateTime(),
                rs.getInt("Variacao_id")
            );
        }
    }
}
