--liquibase formatted sql

--changeset CarlosEduardo:1472081571244-1
CREATE TABLE outros_movimentos (id BIGSERIAL NOT NULL, tipo_operacao_caixa SMALLINT NOT NULL, id_terminal BIGINT, id_funcionario BIGINT NOT NULL, data_hora_mov TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, data_estorno date, valor_movimentado numeric(15, 2) DEFAULT 0 NOT NULL, observacoes TEXT, data_cadastro TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, id_entidade BIGINT NOT NULL, CONSTRAINT outros_movimentos_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1472081571244-2
ALTER TABLE outros_movimentos ADD CONSTRAINT outros_movimentos_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1472081571244-3
ALTER TABLE outros_movimentos ADD CONSTRAINT outros_movimentos_id_funcionario_fkey FOREIGN KEY (id_funcionario) REFERENCES funcionarios (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1472081571244-4
ALTER TABLE outros_movimentos ADD CONSTRAINT outros_movimentos_id_terminal_fkey FOREIGN KEY (id_terminal) REFERENCES terminais (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

