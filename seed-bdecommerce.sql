USE `bdecommerce`;

-- Seed realistico para Marcel Pechinchas.
-- Compatível com o schema atual usado pela aplicação.
-- Antes de rodar, garanta que as migracoes recentes ja foram aplicadas:
-- usuario.cpf BIGINT, usuario.senha VARCHAR(100), endereco sem acento,
-- Produto.descricao, Produto.usuario_cpf, Variacao.foto_url VARCHAR(500),
-- cupom.codigo VARCHAR(20) e pedido.cupom_codigo VARCHAR(20).
-- Senhas: marcel123 para o gerente, teste123 para os demais usuarios.

-- Ajustes seguros para campos que costumam ficar menores no script gerado pelo Workbench.
-- Se alguma coluna abaixo ja estiver com esse tamanho, o comando apenas mantém o formato esperado.
ALTER TABLE `usuario`
  MODIFY COLUMN `senha` VARCHAR(100) NOT NULL;

ALTER TABLE `Produto`
  MODIFY COLUMN `descricao` VARCHAR(500) NOT NULL;

ALTER TABLE `Variacao`
  MODIFY COLUMN `foto_url` VARCHAR(500) NOT NULL;

-- A coluna Produto.usuario_cpf deve existir para a pagina /minha-conta listar produtos criados.
-- Caso ainda nao exista no seu banco, rode antes do seed:
-- ALTER TABLE `Produto` ADD COLUMN `usuario_cpf` BIGINT(20) NULL AFTER `categoria_id`;
-- ALTER TABLE `Produto` ADD INDEX `fk_Produto_usuario1_idx` (`usuario_cpf`);
-- ALTER TABLE `Produto` ADD CONSTRAINT `fk_Produto_usuario1` FOREIGN KEY (`usuario_cpf`) REFERENCES `usuario` (`cpf`) ON DELETE SET NULL ON UPDATE NO ACTION;

INSERT INTO `usuario` (`cpf`, `email`, `senha`, `username`, `gerente`) VALUES
(11111111111, 'marcel@pechinchas.com', '$2a$10$h3XDmaRoxy2zCFAfNjI1x.tUvSmsQTJd5oTGC8AjC.bMdfvYuMKIm', 'marcel', 1),
(22222222222, 'ana@email.com', '$2a$10$q5wnQnMHKjRmFdbAFuGWEeRtieKjYVv0yAKDH2luoGrbPCVrJkxA2', 'ana.compra', 0),
(33333333333, 'bruno@email.com', '$2a$10$q5wnQnMHKjRmFdbAFuGWEeRtieKjYVv0yAKDH2luoGrbPCVrJkxA2', 'bruno.tech', 0),
(44444444444, 'carla@email.com', '$2a$10$q5wnQnMHKjRmFdbAFuGWEeRtieKjYVv0yAKDH2luoGrbPCVrJkxA2', 'carla.casa', 0)
ON DUPLICATE KEY UPDATE email = VALUES(email), senha = VALUES(senha), username = VALUES(username), gerente = VALUES(gerente);

INSERT INTO `endereco` (`id`, `logradouro`, `numero`, `bairro`, `cep`, `nome`, `usuario_cpf`) VALUES
(101, 'Rua das Palmeiras', 120, 'Centro', 88010010, 'Casa', 22222222222),
(102, 'Avenida Brasil', 450, 'Jardins', 88020020, 'Trabalho', 22222222222),
(103, 'Rua do Sol', 77, 'Vila Nova', 88030030, 'Apartamento', 33333333333),
(104, 'Travessa das Flores', 15, 'Boa Vista', 88040040, 'Casa', 44444444444),
(105, 'Avenida das Lojas', 999, 'Comercial', 88050050, 'Marcel Pechinchas', 11111111111)
ON DUPLICATE KEY UPDATE logradouro = VALUES(logradouro), numero = VALUES(numero), bairro = VALUES(bairro), cep = VALUES(cep), nome = VALUES(nome), usuario_cpf = VALUES(usuario_cpf);

INSERT INTO `categoria` (`id`, `descricao`) VALUES
(1, 'Moda'),
(2, 'Calcados'),
(3, 'Acessorios'),
(4, 'Eletronicos'),
(5, 'Casa'),
(6, 'Escritorio')
ON DUPLICATE KEY UPDATE descricao = VALUES(descricao);

INSERT INTO `Produto` (`id`, `marca`, `nome`, `fabricante`, `categoria_id`, `descricao`, `usuario_cpf`) VALUES
(1001, 'Urban Leaf', 'Camiseta Oversized Verde', 'Urban Leaf Studio', 1, 'Camiseta de algodao pesado com caimento amplo para uso diario.', 22222222222),
(1002, 'Rua Clara', 'Tenis Urbano Areia', 'Rua Clara Calcados', 2, 'Tenis casual de solado macio para rotina urbana.', 22222222222),
(1003, 'Lona Norte', 'Mochila Lona Campo', 'Lona Norte Bags', 3, 'Mochila resistente com bolsos externos e espaco para notebook.', 33333333333),
(1004, 'WaveBeat', 'Headphone Bluetooth Studio', 'WaveBeat Audio', 4, 'Headphone sem fio com cancelamento passivo e bateria longa.', 33333333333),
(1005, 'NovaTech', 'Smartphone Nova X', 'NovaTech Mobile', 4, 'Smartphone com tela grande, camera dupla e carregamento rapido.', 11111111111),
(1006, 'Minuto', 'Relogio Minimalista Preto', 'Minuto Design', 3, 'Relogio analogico leve com pulseira de couro sintetico.', 44444444444),
(1007, 'Casa Forte', 'Cafeteira Inox 800ml', 'Casa Forte Utilidades', 5, 'Cafeteira compacta em inox para cozinha pequena.', 44444444444),
(1008, 'Luz Baixa', 'Luminaria de Mesa Articulada', 'Luz Baixa Decor', 5, 'Luminaria articulada com foco ajustavel para estudos.', 11111111111),
(1009, 'Vento Sul', 'Jaqueta Corta Vento Mostarda', 'Vento Sul Apparel', 1, 'Jaqueta leve com capuz e bolsos laterais.', 22222222222),
(1010, 'TermoGo', 'Garrafa Termica Matte', 'TermoGo Brasil', 3, 'Garrafa termica com acabamento fosco e tampa de rosca.', 33333333333),
(1011, 'KeyLab', 'Teclado Mecanico Compacto', 'KeyLab Perifericos', 6, 'Teclado compacto com switches tateis e iluminacao branca.', 11111111111),
(1012, 'Ordem Simples', 'Kit Organizadores de Gaveta', 'Ordem Simples Casa', 5, 'Kit modular para organizar roupas, cabos e acessorios.', 44444444444)
ON DUPLICATE KEY UPDATE marca = VALUES(marca), nome = VALUES(nome), fabricante = VALUES(fabricante), categoria_id = VALUES(categoria_id), descricao = VALUES(descricao), usuario_cpf = VALUES(usuario_cpf);

INSERT INTO `atributo` (`id`, `valor`, `key`) VALUES
(2001, 'algodao pesado', 'tecido'),
(2002, 'oversized', 'modelagem'),
(2003, 'borracha', 'solado'),
(2004, 'lona encerada', 'material'),
(2005, 'bluetooth 5.3', 'conectividade'),
(2006, '128gb', 'armazenamento'),
(2007, 'couro sintetico', 'pulseira'),
(2008, 'inox', 'material'),
(2009, 'led', 'tipo de luz'),
(2010, 'nylon leve', 'tecido'),
(2011, '500ml', 'capacidade'),
(2012, 'switch tatil', 'teclas'),
(2013, 'plastico rigido', 'material')
ON DUPLICATE KEY UPDATE valor = VALUES(valor), `key` = VALUES(`key`);

INSERT INTO `especifica` (`Produto_id`, `atributo_id`) VALUES
(1001, 2001), (1001, 2002),
(1002, 2003),
(1003, 2004),
(1004, 2005),
(1005, 2006),
(1006, 2007),
(1007, 2008),
(1008, 2009),
(1009, 2010),
(1010, 2011),
(1011, 2012),
(1012, 2013)
ON DUPLICATE KEY UPDATE atributo_id = VALUES(atributo_id);

INSERT INTO `Variacao` (`id`, `nome`, `preco`, `estoque`, `foto_url`, `Produto_id`) VALUES
(3001, 'P / Verde Oliva', 79.90, 18, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=900&q=80', 1001),
(3002, 'G / Verde Oliva', 79.90, 12, 'https://images.unsplash.com/photo-1503342217505-b0a15ec3261c?auto=format&fit=crop&w=900&q=80', 1001),
(3003, '38 / Areia', 189.90, 9, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=900&q=80', 1002),
(3004, '42 / Areia', 189.90, 7, 'https://images.unsplash.com/photo-1549298916-b41d501d3772?auto=format&fit=crop&w=900&q=80', 1002),
(3005, '18L / Caqui', 149.90, 14, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=900&q=80', 1003),
(3006, '24L / Preto', 179.90, 8, 'https://images.unsplash.com/photo-1622560480605-d83c853bc5c3?auto=format&fit=crop&w=900&q=80', 1003),
(3007, 'Preto Fosco', 249.90, 16, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=900&q=80', 1004),
(3008, 'Creme', 259.90, 11, 'https://images.unsplash.com/photo-1484704849700-f032a568e944?auto=format&fit=crop&w=900&q=80', 1004),
(3009, '128GB / Grafite', 1499.90, 10, 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=900&q=80', 1005),
(3010, '256GB / Azul', 1799.90, 6, 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?auto=format&fit=crop&w=900&q=80', 1005),
(3011, '40mm / Preto', 119.90, 20, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=80', 1006),
(3012, '36mm / Marrom', 119.90, 15, 'https://images.unsplash.com/photo-1533139502658-0198f920d8e8?auto=format&fit=crop&w=900&q=80', 1006),
(3013, '800ml / Inox', 99.90, 13, 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?auto=format&fit=crop&w=900&q=80', 1007),
(3014, '1L / Inox', 129.90, 9, 'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&fit=crop&w=900&q=80', 1007),
(3015, 'Preta', 139.90, 17, 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=900&q=80', 1008),
(3016, 'Branca', 139.90, 12, 'https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?auto=format&fit=crop&w=900&q=80', 1008),
(3017, 'M / Mostarda', 219.90, 10, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=900&q=80', 1009),
(3018, 'G / Preto', 229.90, 8, 'https://images.unsplash.com/photo-1543076447-215ad9ba6923?auto=format&fit=crop&w=900&q=80', 1009),
(3019, '500ml / Verde', 89.90, 22, 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=900&q=80', 1010),
(3020, '750ml / Preto', 109.90, 14, 'https://images.unsplash.com/photo-1523362628745-0c100150b504?auto=format&fit=crop&w=900&q=80', 1010),
(3021, 'ABNT2 / Preto', 299.90, 7, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=900&q=80', 1011),
(3022, 'US / Branco', 289.90, 5, 'https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?auto=format&fit=crop&w=900&q=80', 1011),
(3023, '6 pecas / Transparente', 69.90, 25, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?auto=format&fit=crop&w=900&q=80', 1012),
(3024, '10 pecas / Cinza', 89.90, 18, 'https://images.unsplash.com/photo-1618220179428-22790b461013?auto=format&fit=crop&w=900&q=80', 1012)
ON DUPLICATE KEY UPDATE nome = VALUES(nome), preco = VALUES(preco), estoque = VALUES(estoque), foto_url = VALUES(foto_url), Produto_id = VALUES(Produto_id);

INSERT INTO `cupom` (`codigo`, `valor`, `quantidade`, `validade`) VALUES
('MARCEL10', 10.00, 100, '2026-12-31 23:59:59'),
('PECHINCHA25', 25.00, 60, '2026-10-31 23:59:59'),
('PRIMEIRACOMPRA', 35.00, 40, '2026-09-30 23:59:59'),
('FRETE30', 30.00, 80, '2026-12-31 23:59:59')
ON DUPLICATE KEY UPDATE valor = VALUES(valor), quantidade = VALUES(quantidade), validade = VALUES(validade);

INSERT INTO `transportadora` (`id`, `nome`) VALUES
(1, 'Correios'),
(2, 'Pechinchas Express')
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

-- Pedidos de exemplo para a pagina /pedidos.
INSERT INTO `carrinho` (`id`, `nome`, `valor_total`, `usuario_cpf`) VALUES
(4001, 'Carrinho de ana.compra', '269.80', 22222222222),
(4002, 'Carrinho de bruno.tech', '1749.80', 33333333333),
(4003, 'Carrinho de carla.casa', '229.80', 44444444444)
ON DUPLICATE KEY UPDATE nome = VALUES(nome), valor_total = VALUES(valor_total), usuario_cpf = VALUES(usuario_cpf);

INSERT INTO `contem` (`Variacao_id`, `carrinho_id`, `quanatidade`) VALUES
(3001, 4001, 1),
(3003, 4001, 1),
(3009, 4002, 1),
(3007, 4002, 1),
(3013, 4003, 1),
(3015, 4003, 1)
ON DUPLICATE KEY UPDATE quanatidade = VALUES(quanatidade);

INSERT INTO `Forma_de_pagamento` (`id`, `Custo_de_operacao`, `endereco_id`) VALUES
(5001, 0, 101),
(5002, 0, 103),
(5003, 0, 104)
ON DUPLICATE KEY UPDATE Custo_de_operacao = VALUES(Custo_de_operacao), endereco_id = VALUES(endereco_id);

INSERT INTO `pix` (`codigo_pix`, `Forma_de_pagamento_id`) VALUES
(700001, 5001),
(700002, 5002)
ON DUPLICATE KEY UPDATE codigo_pix = VALUES(codigo_pix);

INSERT INTO `boleto` (`codigo_de_barras`, `Forma_de_pagamento_id`) VALUES
(800003, 5003)
ON DUPLICATE KEY UPDATE codigo_de_barras = VALUES(codigo_de_barras);

INSERT INTO `pagamento` (`id`, `valor_total`, `data`, `Forma_de_pagamento_id`) VALUES
(6001, 269.80, '2026-07-02 10:15:00', 5001),
(6002, 1749.80, '2026-07-03 14:25:00', 5002),
(6003, 229.80, '2026-07-04 19:40:00', 5003)
ON DUPLICATE KEY UPDATE valor_total = VALUES(valor_total), data = VALUES(data), Forma_de_pagamento_id = VALUES(Forma_de_pagamento_id);

INSERT INTO `entrega` (`id`, `codigo_rastreio`, `previsao`, `status`, `transportadora_id`, `endereco_id`) VALUES
(9001, 12345001, '2026-07-09 18:00:00', 'Preparando envio', 2, 101),
(9002, 12345002, '2026-07-10 18:00:00', 'Enviado', 1, 103),
(9003, 12345003, '2026-07-11 18:00:00', 'Separando pedido', 2, 104)
ON DUPLICATE KEY UPDATE codigo_rastreio = VALUES(codigo_rastreio), previsao = VALUES(previsao), status = VALUES(status), transportadora_id = VALUES(transportadora_id), endereco_id = VALUES(endereco_id);

INSERT INTO `pedido` (`codigo`, `cupom_codigo`, `carrinho_id`, `pagamento_id`, `entrega_id`) VALUES
(9501, 'MARCEL10', 4001, 6001, 9001),
(9502, 'PECHINCHA25', 4002, 6002, 9002),
(9503, 'FRETE30', 4003, 6003, 9003)
ON DUPLICATE KEY UPDATE cupom_codigo = VALUES(cupom_codigo), carrinho_id = VALUES(carrinho_id), pagamento_id = VALUES(pagamento_id), entrega_id = VALUES(entrega_id);
