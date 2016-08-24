--liquibase formatted sql

--changeset CarlosEduardo:1471990432461-1
CREATE TABLE fechamento_terminais (id BIGSERIAL NOT NULL, id_terminal BIGINT NOT NULL, id_funcionario BIGINT NOT NULL, data_encerramento date DEFAULT NOW() NOT NULL, data_estorno date, resto_caixa numeric(15, 2) DEFAULT 0 NOT NULL, total_movimentos_dia numeric(15, 2) DEFAULT 0 NOT NULL, total_creditos_terminal numeric(15, 2) DEFAULT 0 NOT NULL, total_debitos_terminal numeric(15, 2) DEFAULT 0 NOT NULL, saldo_terminal numeric(15, 2) DEFAULT 0 NOT NULL, diferenca_caixa numeric(15, 2) DEFAULT 0 NOT NULL, observacoes TEXT, data_cadastro TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, id_entidade BIGINT NOT NULL, CONSTRAINT fechamento_terminais_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471990432461-2
ALTER TABLE fechamento_terminais ADD CONSTRAINT fechamento_terminais_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471990432461-3
ALTER TABLE fechamento_terminais ADD CONSTRAINT fechamento_terminais_id_funcionario_fkey FOREIGN KEY (id_funcionario) REFERENCES funcionarios (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471990432461-4
ALTER TABLE fechamento_terminais ADD CONSTRAINT fechamento_terminais_id_terminal_fkey FOREIGN KEY (id_terminal) REFERENCES terminais (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

