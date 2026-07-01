package com.ecommerce.repository;

import com.ecommerce.domain.pedido.Pedido;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT codigo, cupom_codigo, carrinho_id, pagamento_id, entrega_id FROM pedido";
    private static final String SELECT_BY_ID = "SELECT codigo, cupom_codigo, carrinho_id, pagamento_id, entrega_id FROM pedido WHERE codigo = ?";
    private static final String INSERT = "INSERT INTO pedido (codigo, cupom_codigo, carrinho_id, pagamento_id, entrega_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE pedido SET cupom_codigo = ?, carrinho_id = ?, pagamento_id = ?, entrega_id = ? WHERE codigo = ?";
    private static final String DELETE = "DELETE FROM pedido WHERE codigo = ?";

    public PedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Pedido> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new PedidoRowMapper());
    }

    public Optional<Pedido> findById(Integer codigo) {
        List<Pedido> pedidos = jdbcTemplate.query(SELECT_BY_ID, new PedidoRowMapper(), codigo);
        return pedidos.stream().findFirst();
    }

    public int save(Pedido pedido) {
        return jdbcTemplate.update(INSERT,
            pedido.codigo(),
            pedido.cupomCodigo(),
            pedido.carrinhoId(),
            pedido.pagamentoId(),
            pedido.entregaId()
        );
    }

    public int update(Pedido pedido) {
        return jdbcTemplate.update(UPDATE,
            pedido.cupomCodigo(),
            pedido.carrinhoId(),
            pedido.pagamentoId(),
            pedido.entregaId(),
            pedido.codigo()
        );
    }

    public int delete(Integer codigo) {
        return jdbcTemplate.update(DELETE, codigo);
    }

    private static class PedidoRowMapper implements RowMapper<Pedido> {
        @Override
        public Pedido mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Pedido(
                rs.getInt("codigo"),
                rs.getInt("cupom_codigo"),
                rs.getInt("carrinho_id"),
                rs.getInt("pagamento_id"),
                rs.getInt("entrega_id")
            );
        }
    }
}
