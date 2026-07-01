package com.ecommerce.repository;

import com.ecommerce.domain.variacao.Variacao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class VariacaoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, nome, preco, estoque, foto_url, Produto_id FROM Variacao";
    private static final String SELECT_BY_ID = "SELECT id, nome, preco, estoque, foto_url, Produto_id FROM Variacao WHERE id = ?";
    private static final String SELECT_BY_PRODUTO = "SELECT id, nome, preco, estoque, foto_url, Produto_id FROM Variacao WHERE Produto_id = ?";
    private static final String INSERT = "INSERT INTO Variacao (id, nome, preco, estoque, foto_url, Produto_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Variacao SET nome = ?, preco = ?, estoque = ?, foto_url = ?, Produto_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Variacao WHERE id = ?";

    public VariacaoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Variacao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new VariacaoRowMapper());
    }

    public Optional<Variacao> findById(Integer id) {
        List<Variacao> variacoes = jdbcTemplate.query(SELECT_BY_ID, new VariacaoRowMapper(), id);
        return variacoes.stream().findFirst();
    }

    public List<Variacao> findByProdutoId(Integer produtoId) {
        return jdbcTemplate.query(SELECT_BY_PRODUTO, new VariacaoRowMapper(), produtoId);
    }

    public int save(Variacao variacao) {
        return jdbcTemplate.update(INSERT,
            variacao.id(),
            variacao.nome(),
            variacao.preco(),
            variacao.estoque(),
            variacao.fotoUrl(),
            variacao.produtoId()
        );
    }

    public int update(Variacao variacao) {
        return jdbcTemplate.update(UPDATE,
            variacao.nome(),
            variacao.preco(),
            variacao.estoque(),
            variacao.fotoUrl(),
            variacao.produtoId(),
            variacao.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class VariacaoRowMapper implements RowMapper<Variacao> {
        @Override
        public Variacao mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Variacao(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getFloat("preco"),
                rs.getInt("estoque"),
                rs.getString("foto_url"),
                rs.getInt("Produto_id")
            );
        }
    }
}
