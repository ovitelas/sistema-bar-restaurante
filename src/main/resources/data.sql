INSERT INTO cliente (nome, telefone) VALUES ('Cliente Teste', '11950502525');

INSERT INTO produto (nome, preco, estoque) VALUES ('Cerveja', 8.90, 50);
INSERT INTO produto (nome, preco, estoque) VALUES ('Refrigerante', 6.50, 100);

INSERT INTO usuario (username, password, role) VALUES ('admin', '{bcrypt}$2a$10$EXEMPLOHASHSENHA...', 'ADMIN');