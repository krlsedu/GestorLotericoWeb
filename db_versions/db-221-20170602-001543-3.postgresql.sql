--liquibase formatted sql
--changeset carloseduardo:1496373348240-1

CREATE SEQUENCE saldo_estoque_funcionario_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE TABLE saldos_estoque_funcionario (
	id bigint DEFAULT nextval('saldo_estoque_funcionario_id_seq'::regclass) NOT NULL,
	id_itens_estoque bigint NOT NULL,
	quantidade_movimentada numeric(15,2) DEFAULT 0 NOT NULL,
	saldo numeric(15,2) DEFAULT 0 NOT NULL,
	data_hora_saldo timestamp without time zone DEFAULT now() NOT NULL,
	data_hora_movimento timestamp without time zone DEFAULT now() NOT NULL,
	id_entidade bigint NOT NULL,
	id_loterica bigint,
	id_movimento bigint,
	observacoes text,
	id_funcionario bigint NOT NULL
);

COMMENT ON COLUMN saldos_estoque_funcionario.data_hora_saldo IS 'data e hora de referência para o movimento';

COMMENT ON COLUMN saldos_estoque_funcionario.data_hora_movimento IS 'data e hora em que o registro foi gerado';

ALTER TABLE saldos_estoque_funcionario OWNER TO postgres;

ALTER TABLE movimentos_estoque
	ADD COLUMN id_funcionario bigint;

ALTER SEQUENCE saldo_estoque_funcionario_id_seq
	OWNED BY saldos_estoque_funcionario.id;

ALTER TABLE saldos_estoque_funcionario
	ADD CONSTRAINT saldo_estoque_funcionario_pkey PRIMARY KEY (id);

ALTER TABLE movimentos_estoque
	ADD CONSTRAINT movimentos_estoque_id_funcionario_fk FOREIGN KEY (id_funcionario) REFERENCES funcionarios(id);

ALTER TABLE saldos_estoque_funcionario
	ADD CONSTRAINT saldos_estoque_funcionario_id_entidade_fk FOREIGN KEY (id_entidade) REFERENCES entidades(id);

ALTER TABLE saldos_estoque_funcionario
	ADD CONSTRAINT saldos_estoque_funcionario_id_funconario_fk FOREIGN KEY (id_funcionario) REFERENCES funcionarios(id);

ALTER TABLE saldos_estoque_funcionario
	ADD CONSTRAINT saldos_estoque_funcionario_id_itens_estoque_fk FOREIGN KEY (id_itens_estoque) REFERENCES itens_estoque(id);

ALTER TABLE saldos_estoque_funcionario
	ADD CONSTRAINT saldos_estoque_funcionario_id_loterica_fk FOREIGN KEY (id_loterica) REFERENCES lotericas(id);

ALTER TABLE saldos_estoque_funcionario
	ADD CONSTRAINT saldos_estoque_funcionario_id_movimento_fk FOREIGN KEY (id_movimento) REFERENCES movimentos_estoque(id);

CREATE UNIQUE INDEX saldo_estoque_funcionario_id_uindex ON saldos_estoque_funcionario USING btree (id);
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '221','20170602-001543'); 
