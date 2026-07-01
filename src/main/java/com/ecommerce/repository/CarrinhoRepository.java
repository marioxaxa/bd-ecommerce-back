package com.ecommerce.repository;

import com.ecommerce.domain.carrinho.Carrinho;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CarrinhoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, nome, valor_total, usuario_cpf FROM carrinho";
    private static final String SELECT_BY_ID = "SELECT id, nome, valor_total, usuario_cpf FROM carrinho WHERE id = ?";
    private static final String SELECT_BY_USUARIO = "SELECT id, nome, valor_total, usuario_cpf FROM carrinho WHERE usuario_cpf = ?";
    private static final String INSERT = "INSERT INTO carrinho (id, nome, valor_total, usuario_cpf) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE carrinho SET nome = ?, valor_total = ?, usuario_cpf = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM carrinho WHERE id = ?";

    public CarrinhoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Carrinho> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new CarrinhoRowMapper());
    }

    public Optional<Carrinho> findById(Integer id) {
        List<Carrinho> carrinhos = jdbcTemplate.query(SELECT_BY_ID, new CarrinhoRowMapper(), id);
        return carrinhos.stream().findFirst();
    }

    public List<Carrinho> findByUsuarioCpf(Integer usuarioCpf) {
        return jdbcTemplate.query(SELECT_BY_USUARIO, new CarrinhoRowMapper(), usuarioCpf);
    }

    public int save(Carrinho carrinho) {
        return jdbcTemplate.update(INSERT,
            carrinho.id(),
            carrinho.nome(),
            carrinho.valorTotal(),
            carrinho.usuarioCpf()
        );
    }

    public int update(Carrinho carrinho) {
        return jdbcTemplate.update(UPDATE,
            carrinho.nome(),
            carrinho.valorTotal(),
            carrinho.usuarioCpf(),
            carrinho.id()
        );
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private static class CarrinhoRowMapper implements RowMapper<Carrinho> {
        @Override
        public Carrinho mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Carrinho(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("valor_total"),
                rs.getInt("usuario_cpf")
            );
        }
    }
}
