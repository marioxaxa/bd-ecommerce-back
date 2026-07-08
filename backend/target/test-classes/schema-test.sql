CREATE TABLE IF NOT EXISTS usuario (
  cpf BIGINT NOT NULL,
  email VARCHAR(45) NOT NULL,
  senha VARCHAR(100) NOT NULL,
  username VARCHAR(45) NOT NULL,
  gerente TINYINT NOT NULL,
  PRIMARY KEY (cpf)
);

CREATE TABLE IF NOT EXISTS endereco (
  id INT NOT NULL,
  logradouro VARCHAR(45) NOT NULL,
  numero INT NOT NULL,
  bairro VARCHAR(45) NOT NULL,
  cep INT NOT NULL,
  nome VARCHAR(45) NOT NULL,
  usuario_cpf BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_endereco_usuario FOREIGN KEY (usuario_cpf) REFERENCES usuario(cpf)
);

CREATE TABLE IF NOT EXISTS Forma_de_pagamento (
  id INT NOT NULL,
  Custo_de_operacao INT NOT NULL,
  endereco_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_forma_pagamento_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

CREATE TABLE IF NOT EXISTS categoria (
  id INT NOT NULL,
  descricao VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Produto (
  id INT NOT NULL,
  marca VARCHAR(45) NOT NULL,
  nome VARCHAR(45) NOT NULL,
  fabricante VARCHAR(45) NOT NULL,
  categoria_id INT NOT NULL,
  descricao VARCHAR(255),
  usuario_cpf BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id),
  CONSTRAINT fk_produto_usuario FOREIGN KEY (usuario_cpf) REFERENCES usuario(cpf)
);

CREATE TABLE IF NOT EXISTS atributo (
  id INT NOT NULL,
  valor VARCHAR(45) NOT NULL,
  `key` VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Variacao (
  id INT NOT NULL,
  nome VARCHAR(45) NOT NULL,
  preco FLOAT NOT NULL,
  estoque INT NOT NULL,
  foto_url VARCHAR(45) NOT NULL,
  Produto_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_variacao_produto FOREIGN KEY (Produto_id) REFERENCES Produto(id)
);

CREATE TABLE IF NOT EXISTS carrinho (
  id INT NOT NULL,
  nome VARCHAR(45) NOT NULL,
  valor_total VARCHAR(45) NOT NULL,
  usuario_cpf BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_carrinho_usuario FOREIGN KEY (usuario_cpf) REFERENCES usuario(cpf)
);

CREATE TABLE IF NOT EXISTS cupom (
  codigo VARCHAR(45) NOT NULL,
  valor FLOAT NOT NULL,
  quantidade INT NOT NULL,
  validade DATETIME NOT NULL,
  PRIMARY KEY (codigo)
);

CREATE TABLE IF NOT EXISTS transportadora (
  id INT NOT NULL,
  nome VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS entrega (
  id INT NOT NULL,
  codigo_rastreio INT NOT NULL,
  previsao DATETIME NOT NULL,
  status VARCHAR(45) NOT NULL,
  transportadora_id INT NOT NULL,
  endereco_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_entrega_transportadora FOREIGN KEY (transportadora_id) REFERENCES transportadora(id),
  CONSTRAINT fk_entrega_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

CREATE TABLE IF NOT EXISTS pagamento (
  id INT NOT NULL,
  valor_total FLOAT NOT NULL,
  data DATETIME NOT NULL,
  Forma_de_pagamento_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_pagamento_forma FOREIGN KEY (Forma_de_pagamento_id) REFERENCES Forma_de_pagamento(id)
);

CREATE TABLE IF NOT EXISTS pedido (
  codigo INT NOT NULL,
  cupom_codigo VARCHAR(45) NOT NULL,
  carrinho_id INT NOT NULL,
  pagamento_id INT NOT NULL,
  entrega_id INT NOT NULL,
  PRIMARY KEY (codigo),
  CONSTRAINT fk_pedido_cupom FOREIGN KEY (cupom_codigo) REFERENCES cupom(codigo),
  CONSTRAINT fk_pedido_entrega FOREIGN KEY (entrega_id) REFERENCES entrega(id),
  CONSTRAINT fk_pedido_carrinho FOREIGN KEY (carrinho_id) REFERENCES carrinho(id),
  CONSTRAINT fk_pedido_pagamento FOREIGN KEY (pagamento_id) REFERENCES pagamento(id)
);

CREATE TABLE IF NOT EXISTS especifica (
  Produto_id INT NOT NULL,
  atributo_id INT NOT NULL,
  PRIMARY KEY (Produto_id, atributo_id),
  CONSTRAINT fk_especifica_atributo FOREIGN KEY (atributo_id) REFERENCES atributo(id),
  CONSTRAINT fk_especifica_produto FOREIGN KEY (Produto_id) REFERENCES Produto(id)
);

CREATE TABLE IF NOT EXISTS tem_atributo (
  Variacao_id INT NOT NULL,
  atributo_id INT NOT NULL,
  PRIMARY KEY (Variacao_id, atributo_id),
  CONSTRAINT fk_tem_atributo_variacao FOREIGN KEY (Variacao_id) REFERENCES Variacao(id),
  CONSTRAINT fk_tem_atributo_atributo FOREIGN KEY (atributo_id) REFERENCES atributo(id)
);

CREATE TABLE IF NOT EXISTS contem (
  Variacao_id INT NOT NULL,
  carrinho_id INT NOT NULL,
  quanatidade INT NOT NULL,
  PRIMARY KEY (Variacao_id, carrinho_id),
  CONSTRAINT fk_contem_variacao FOREIGN KEY (Variacao_id) REFERENCES Variacao(id),
  CONSTRAINT fk_contem_carrinho FOREIGN KEY (carrinho_id) REFERENCES carrinho(id)
);

CREATE TABLE IF NOT EXISTS Preco_historico (
  id INT NOT NULL,
  valor FLOAT NOT NULL,
  data_inicio DATETIME NOT NULL,
  data_fim DATETIME NOT NULL,
  Variacao_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_preco_variacao FOREIGN KEY (Variacao_id) REFERENCES Variacao(id)
);

CREATE TABLE IF NOT EXISTS pix (
  codigo_pix INT NOT NULL,
  Forma_de_pagamento_id INT NOT NULL,
  PRIMARY KEY (Forma_de_pagamento_id),
  CONSTRAINT fk_pix_forma FOREIGN KEY (Forma_de_pagamento_id) REFERENCES Forma_de_pagamento(id)
);

CREATE TABLE IF NOT EXISTS boleto (
  codigo_de_barras INT NOT NULL,
  Forma_de_pagamento_id INT NOT NULL,
  PRIMARY KEY (Forma_de_pagamento_id),
  CONSTRAINT fk_boleto_forma FOREIGN KEY (Forma_de_pagamento_id) REFERENCES Forma_de_pagamento(id)
);

CREATE TABLE IF NOT EXISTS cartao (
  numero INT NOT NULL,
  Forma_de_pagamento_id INT NOT NULL,
  titular VARCHAR(45) NOT NULL,
  validade DATETIME NOT NULL,
  cvv INT NOT NULL,
  bandeira VARCHAR(45) NOT NULL,
  numero_parcelas INT NOT NULL,
  PRIMARY KEY (Forma_de_pagamento_id),
  CONSTRAINT fk_cartao_forma FOREIGN KEY (Forma_de_pagamento_id) REFERENCES Forma_de_pagamento(id)
);
