--liquibase formatted sql

--changeset CarlosEduardo:1471468915354-1
INSERT INTO cols_descri (id, coluna, descricao) VALUES (1, 'id', 'Código');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (2, 'codigo_caixa', 'Código na Caixa');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (3, 'nome', 'Nome');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (4, 'id_loterica', 'Código da lotérica (Sistema)');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (5, 'marca', 'Marca');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (6, 'modelo', 'Modelo');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (7, 'observacoes', 'Observações');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (8, 'cpf', 'CPF');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (9, 'tipo', 'Tipo de Cadastro');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (10, 'tipo_func', 'Tipo Funcionário');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (11, 'nome_conta', 'Nome da conta');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (12, 'conta_corrente', 'Número da Conta');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (13, 'operacao', 'Operação');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (14, 'nome_oper_caixa', 'Nome da Operação (Caixa)');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (15, 'nome_oper', 'Nome da Operação');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (16, 'tipo_oper', 'Tipo Operação');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (17, 'nome_cofre', 'Nome do Cofre');
INSERT INTO cols_descri (id, coluna, descricao) VALUES (18, 'tipo_cofre', 'Tipo Cofre');

--changeset CarlosEduardo:1471468915354-2
INSERT INTO entidades (id, nome, tipo) VALUES (2, 'Testes', 1);

--changeset CarlosEduardo:1471468915354-3
INSERT INTO usuarios (id, codigo_caixa, usuario, senha, tipo, nome, email, telefone) VALUES (1, 0, 'krlsedu                                           ', '270713', 0, 'Carlos Eduardo Duarte Schwalm                                                                                                                                                                           ', 'krlsedu@gmail.com                                                                                                                                                                                       ', '5198212766                                        ');

--changeset CarlosEduardo:1471468915354-4
INSERT INTO usuarios_entidades (id, id_usuario, id_entidade) VALUES (1, 1, 2);

