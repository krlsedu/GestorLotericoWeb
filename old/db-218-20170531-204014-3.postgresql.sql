--liquibase formatted sql
--changeset carloseduardo:1496274020134-1

CREATE SEQUENCE movimentos_estoque_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE saldo_estoque_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE TABLE movimentos_estoque (
	id bigint DEFAULT nextval('movimentos_estoque_id_seq'::regclass) NOT NULL,
	tipo_movimento integer NOT NULL,
	id_itens_estoque bigint NOT NULL,
	quantidade_movimentada numeric(15,2) DEFAULT 0 NOT NULL,
	id_loterica bigint,
	numero_volumes bigint DEFAULT 1 NOT NULL,
	id_entidade bigint NOT NULL,
	observacoes text,
	data_hora_movimento timestamp without time zone DEFAULT now() NOT NULL,
	data_hora_mov timestamp without time zone DEFAULT now() NOT NULL
);

COMMENT ON COLUMN movimentos_estoque.data_hora_movimento IS 'Data e hora em que o registro foi gerado';

COMMENT ON COLUMN movimentos_estoque.data_hora_mov IS 'data de referencia do movimento
';

ALTER TABLE movimentos_estoque OWNER TO postgres;

CREATE TABLE saldos_estoque (
	id bigint DEFAULT nextval('saldo_estoque_id_seq'::regclass) NOT NULL,
	id_itens_estoque bigint NOT NULL,
	quantidade_movimentada numeric(15,2) DEFAULT 0 NOT NULL,
	saldo numeric(15,2) DEFAULT 0 NOT NULL,
	data_hora_saldo timestamp without time zone DEFAULT now() NOT NULL,
	data_hora_movimento timestamp without time zone DEFAULT now() NOT NULL,
	id_entidade bigint NOT NULL,
	id_loterica bigint,
	id_movimento bigint,
	observacoes text
);

COMMENT ON COLUMN saldos_estoque.data_hora_saldo IS 'data e hora de referência para o movimento';

COMMENT ON COLUMN saldos_estoque.data_hora_movimento IS 'data e hora em que o registro foi gerado';

ALTER TABLE saldos_estoque OWNER TO postgres;

ALTER SEQUENCE movimentos_estoque_id_seq
	OWNED BY movimentos_estoque.id;

ALTER SEQUENCE saldo_estoque_id_seq
	OWNED BY saldos_estoque.id;

ALTER TABLE movimentos_estoque
	ADD CONSTRAINT movimentos_estoque_pkey PRIMARY KEY (id);

ALTER TABLE saldos_estoque
	ADD CONSTRAINT saldo_estoque_pkey PRIMARY KEY (id);

ALTER TABLE movimentos_estoque
	ADD CONSTRAINT movimentos_estoque_id_entidade_fk FOREIGN KEY (id_entidade) REFERENCES entidades(id);

ALTER TABLE movimentos_estoque
	ADD CONSTRAINT movimentos_estoque_id_itens_estoque_fk FOREIGN KEY (id_itens_estoque) REFERENCES itens_estoque(id);

ALTER TABLE movimentos_estoque
	ADD CONSTRAINT movimentos_estoque_id_loterica_fk FOREIGN KEY (id_loterica) REFERENCES lotericas(id);

ALTER TABLE saldos_estoque
	ADD CONSTRAINT saldos_estoque_id_entidade_fk FOREIGN KEY (id_entidade) REFERENCES entidades(id);

ALTER TABLE saldos_estoque
	ADD CONSTRAINT saldos_estoque_id_itens_estoque_fk FOREIGN KEY (id_itens_estoque) REFERENCES itens_estoque(id);

ALTER TABLE saldos_estoque
	ADD CONSTRAINT saldos_estoque_id_loterica_fk FOREIGN KEY (id_loterica) REFERENCES lotericas(id);

ALTER TABLE saldos_estoque
	ADD CONSTRAINT saldos_estoque_id_movimento_fk FOREIGN KEY (id_movimento) REFERENCES movimentos_estoque(id);

CREATE UNIQUE INDEX itens_estoque_id_uindex ON itens_estoque USING btree (id);

CREATE UNIQUE INDEX movimentos_estoque_id_uindex ON movimentos_estoque USING btree (id);

CREATE UNIQUE INDEX saldo_estoque_id_uindex ON saldos_estoque USING btree (id);
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '218','20170531-204014'); 
