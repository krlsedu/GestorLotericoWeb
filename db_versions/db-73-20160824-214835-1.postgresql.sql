--liquibase formatted sql

--changeset CarlosEduardo:1472086119813-1
CREATE TABLE tarifas_operacoes (id BIGSERIAL NOT NULL, id_operacao BIGINT NOT NULL, data_base date DEFAULT NOW() NOT NULL, data_estorno date, valor_tarifa numeric(15, 2) DEFAULT 0 NOT NULL, observacoes TEXT, data_cadastro TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, id_entidade BIGINT NOT NULL, CONSTRAINT tarifas_operacoes_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1472086119813-2
ALTER TABLE tarifas_operacoes ADD CONSTRAINT tarifas_operacoes_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1472086119813-3
ALTER TABLE tarifas_operacoes ADD CONSTRAINT tarifas_operacoes_id_funcionario_fkey FOREIGN KEY (id_operacao) REFERENCES operacoes (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

