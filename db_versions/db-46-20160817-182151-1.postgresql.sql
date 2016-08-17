--liquibase formatted sql

--changeset CarlosEduardo:1471468914046-1
CREATE TABLE abertura_terminais (id BIGSERIAL NOT NULL, id_loterica BIGINT NOT NULL, id_terminal BIGINT NOT NULL, id_funcionario BIGINT NOT NULL, data_abertura date DEFAULT NOW() NOT NULL, data_estorno date, troco_dia_anterior numeric(15, 2) DEFAULT 0 NOT NULL, troco_dia numeric(15, 2) DEFAULT 0 NOT NULL, observacoes TEXT, data_cadastro TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, id_entidade BIGINT NOT NULL, CONSTRAINT aberturas_terminais_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-2
CREATE TABLE cofres (id BIGSERIAL NOT NULL, nome_cofre VARCHAR(200) NOT NULL, tipo_cofre SMALLINT NOT NULL, observacoes TEXT, id_loterica BIGINT NOT NULL, id_entidade BIGINT NOT NULL, CONSTRAINT cofres_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-3
CREATE TABLE cols_descri (id BIGSERIAL NOT NULL, coluna VARCHAR(200) NOT NULL, descricao VARCHAR(1000) NOT NULL, CONSTRAINT cols_descri_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-4
CREATE TABLE contas (id BIGSERIAL NOT NULL, conta_corrente VARCHAR(200) NOT NULL, dv VARCHAR(20) NOT NULL, nome_conta VARCHAR(200) NOT NULL, operacao VARCHAR(200) NOT NULL, agencia VARCHAR(200) NOT NULL, telefone VARCHAR(200) NOT NULL, gerente VARCHAR(200) NOT NULL, id_loterica BIGINT NOT NULL, observacoes TEXT, id_entidade BIGINT NOT NULL, CONSTRAINT contas_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-5
CREATE TABLE db_version (id BIGSERIAL NOT NULL, versao_major VARCHAR(200) NOT NULL, versao_minor VARCHAR(200) NOT NULL, build VARCHAR(500) NOT NULL, compilation VARCHAR(200), CONSTRAINT pk_db_version PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-6
CREATE TABLE entidades (id BIGSERIAL NOT NULL, nome VARCHAR NOT NULL, tipo SMALLINT DEFAULT 1 NOT NULL, CONSTRAINT entidades_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-7
CREATE TABLE erros (id BIGSERIAL NOT NULL, id_usuario BIGINT NOT NULL, id_entidade BIGINT NOT NULL, id_sessao CHAR(200) NOT NULL, ip CHAR(200) NOT NULL, nome_maquina CHAR(200) NOT NULL, pg_anterior TEXT, pg_atual TEXT, data_erro TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, erro TEXT, stacktrace TEXT, CONSTRAINT erros_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-8
CREATE TABLE funcionarios (id BIGSERIAL NOT NULL, codigo_caixa VARCHAR(200) NOT NULL, nome VARCHAR(200) NOT NULL, cpf VARCHAR(20) NOT NULL, tipo_func SMALLINT DEFAULT 1 NOT NULL, id_entidade BIGINT NOT NULL, observacoes TEXT, CONSTRAINT funcionarios_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-9
CREATE TABLE logins (id BIGSERIAL NOT NULL, usuario CHAR(50) NOT NULL, ip CHAR(200) NOT NULL, nome_maquina CHAR(200) NOT NULL, sucesso CHAR(1) DEFAULT 'S', datatentativa TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, CONSTRAINT logins_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-10
CREATE TABLE lotericas (id BIGSERIAL NOT NULL, codigo_caixa VARCHAR(200) NOT NULL, nome VARCHAR(200) NOT NULL, id_entidade BIGINT DEFAULT 2 NOT NULL, CONSTRAINT lotericas_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-11
CREATE TABLE operacoes (id BIGSERIAL NOT NULL, nome_oper_caixa VARCHAR(200) NOT NULL, nome_oper VARCHAR(200) NOT NULL, tipo_oper SMALLINT NOT NULL, observacoes TEXT, id_entidade BIGINT DEFAULT 2 NOT NULL, CONSTRAINT operacoes_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-12
CREATE TABLE sessoes (id BIGSERIAL NOT NULL, id_usuario BIGINT NOT NULL, id_entidade BIGINT NOT NULL, id_sessao CHAR(200) NOT NULL, ip CHAR(200) NOT NULL, nome_maquina CHAR(200) NOT NULL, pg_anterior TEXT, pg_atual TEXT, tempo_sessao SMALLINT DEFAULT 0 NOT NULL, data_acesso TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, CONSTRAINT sessoes_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-13
CREATE TABLE terminais (id BIGSERIAL NOT NULL, codigo_caixa VARCHAR(200) NOT NULL, nome VARCHAR(200) NOT NULL, marca VARCHAR(200), modelo VARCHAR(200), observacoes TEXT, id_loterica BIGINT NOT NULL, id_entidade BIGINT NOT NULL, CONSTRAINT terminais_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-14
CREATE TABLE usuarios (id BIGSERIAL NOT NULL, codigo_caixa BIGINT, usuario CHAR(50) NOT NULL, senha TEXT NOT NULL, tipo SMALLINT DEFAULT 1 NOT NULL, nome CHAR(200) NOT NULL, email CHAR(200) NOT NULL, telefone CHAR(50), CONSTRAINT users_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-15
CREATE TABLE usuarios_entidades (id BIGSERIAL NOT NULL, id_usuario BIGINT NOT NULL, id_entidade BIGINT NOT NULL, CONSTRAINT usuarios_entidades_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471468914046-16
ALTER TABLE entidades ADD CONSTRAINT entidades_nome_key UNIQUE (nome);

--changeset CarlosEduardo:1471468914046-17
ALTER TABLE usuarios ADD CONSTRAINT users_usuario_key UNIQUE (usuario);

--changeset CarlosEduardo:1471468914046-18
ALTER TABLE abertura_terminais ADD CONSTRAINT aberturas_terminais_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-19
ALTER TABLE abertura_terminais ADD CONSTRAINT aberturas_terminais_id_funcionario_fkey FOREIGN KEY (id_funcionario) REFERENCES funcionarios (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-20
ALTER TABLE abertura_terminais ADD CONSTRAINT aberturas_terminais_id_loterica_fkey FOREIGN KEY (id_loterica) REFERENCES lotericas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-21
ALTER TABLE abertura_terminais ADD CONSTRAINT aberturas_terminais_id_terminal_fkey FOREIGN KEY (id_terminal) REFERENCES terminais (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-22
ALTER TABLE cofres ADD CONSTRAINT cofres_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-23
ALTER TABLE cofres ADD CONSTRAINT cofres_id_loterica_fkey FOREIGN KEY (id_loterica) REFERENCES lotericas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-24
ALTER TABLE contas ADD CONSTRAINT contas_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-25
ALTER TABLE contas ADD CONSTRAINT contas_id_loterica_fkey FOREIGN KEY (id_loterica) REFERENCES lotericas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-26
ALTER TABLE funcionarios ADD CONSTRAINT funcionarios_id_loterica_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-27
ALTER TABLE lotericas ADD CONSTRAINT lotericas_i_entidades_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-28
ALTER TABLE operacoes ADD CONSTRAINT operacoes_i_entidades_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-29
ALTER TABLE terminais ADD CONSTRAINT terminais_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-30
ALTER TABLE terminais ADD CONSTRAINT terminais_id_loterica_fkey FOREIGN KEY (id_loterica) REFERENCES lotericas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-31
ALTER TABLE usuarios_entidades ADD CONSTRAINT usuarios_entidades_i_entidades_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471468914046-32
ALTER TABLE usuarios_entidades ADD CONSTRAINT usuarios_entidades_i_usuarios_fkey FOREIGN KEY (id_usuario) REFERENCES usuarios (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

