--liquibase formatted sql

--changeset CarlosEduardo:1471474692270-1
CREATE TABLE movimentos_caixas (id BIGSERIAL NOT NULL, tipo_operacao_caixa SMALLINT NOT NULL, id_terminal BIGINT, id_funcionario BIGINT NOT NULL, data_hora_mov TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, data_estorno date, valor_movimentado numeric(15, 2) DEFAULT 0 NOT NULL, observacoes TEXT, data_cadastro TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, id_entidade BIGINT NOT NULL, CONSTRAINT movimentos_caixas_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1471474692270-2
ALTER TABLE movimentos_caixas ADD CONSTRAINT movimentos_caixas_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471474692270-3
ALTER TABLE movimentos_caixas ADD CONSTRAINT movimentos_caixas_id_funcionario_fkey FOREIGN KEY (id_funcionario) REFERENCES funcionarios (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1471474692270-4
ALTER TABLE movimentos_caixas ADD CONSTRAINT movimentos_caixas_id_terminal_fkey FOREIGN KEY (id_terminal) REFERENCES terminais (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

