--liquibase formatted sql
--changeset carloseduardo:1496806486760-1

DROP INDEX itens_estoque_nome_item_uindex;

CREATE SEQUENCE operacoes_funcionario_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE TABLE operacoes_funcionario (
	id bigint DEFAULT nextval('operacoes_funcionario_id_seq'::regclass) NOT NULL,
	tipo_item integer NOT NULL,
	tipo_operacao_caixa integer NOT NULL,
	edicao_item bigint,
	nome_concurso text,
	data_sorteio date,
	valor_movimentado numeric(15,2),
	observacoes text,
	id_fucionario bigint NOT NULL,
	id_terminal bigint NOT NULL,
	id_abertura_terminal bigint NOT NULL,
	id_entidade bigint NOT NULL,
	quantidade bigint DEFAULT 1,
	data_hora_mov timestamp without time zone DEFAULT now()
);

ALTER TABLE operacoes_funcionario OWNER TO postgres;

ALTER TABLE funcionarios
	ADD COLUMN id_usuario bigint;

ALTER TABLE itens_estoque
	ADD COLUMN data_sorteio date;

ALTER TABLE movimentos_caixas
	DROP COLUMN valor_moeda,
	ADD COLUMN tipo_moeda bigint,
	ADD COLUMN id_operacao_funcionario bigint;

ALTER SEQUENCE operacoes_funcionario_id_seq
	OWNED BY operacoes_funcionario.id;

ALTER TABLE operacoes_funcionario
	ADD CONSTRAINT operacoes_funcionario_pkey PRIMARY KEY (id);

ALTER TABLE funcionarios
	ADD CONSTRAINT funcionarios_id_usuario_fk FOREIGN KEY (id_usuario) REFERENCES usuarios(id);

ALTER TABLE movimentos_caixas
	ADD CONSTRAINT movimentos_caixas_id_operacao_funcionario_fk FOREIGN KEY (id_operacao_funcionario) REFERENCES operacoes_funcionario(id);

ALTER TABLE movimentos_caixas
	ADD CONSTRAINT movimentos_caixas_tipo_moeda_fk FOREIGN KEY (tipo_moeda) REFERENCES itens_estoque(id);

ALTER TABLE operacoes_funcionario
	ADD CONSTRAINT operacoes_funcionario_id_abertura_terminais_fk FOREIGN KEY (id_abertura_terminal) REFERENCES abertura_terminais(id);

ALTER TABLE operacoes_funcionario
	ADD CONSTRAINT operacoes_funcionario_id_entidade_fk FOREIGN KEY (id_entidade) REFERENCES entidades(id);

ALTER TABLE operacoes_funcionario
	ADD CONSTRAINT operacoes_funcionario_id_funcionario_fk FOREIGN KEY (id_fucionario) REFERENCES funcionarios(id);

ALTER TABLE operacoes_funcionario
	ADD CONSTRAINT operacoes_funcionario_id_terminal_fk FOREIGN KEY (id_terminal) REFERENCES terminais(id);

CREATE UNIQUE INDEX operacoes_funcionario_id_uindex ON operacoes_funcionario USING btree (id);
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '224','20170607-003440'); 
