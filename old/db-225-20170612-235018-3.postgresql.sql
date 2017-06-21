--liquibase formatted sql
--changeset carloseduardo:1497322223076-1

ALTER TABLE itens_estoque
	ALTER COLUMN valor_padrao TYPE numeric(15,4) /* TYPE change - table: itens_estoque original: numeric(15,2) new: numeric(15,4) */;

ALTER TABLE movimentos_estoque
	ADD COLUMN id_outros_movimentos bigint,
	ALTER COLUMN quantidade_movimentada TYPE numeric(15,4) /* TYPE change - table: movimentos_estoque original: numeric(15,2) new: numeric(15,4) */;

ALTER TABLE operacoes_funcionario
	ALTER COLUMN valor_movimentado TYPE numeric(15,4) /* TYPE change - table: operacoes_funcionario original: numeric(15,2) new: numeric(15,4) */;

ALTER TABLE outros_movimentos
	ADD COLUMN id_operacao_funcionario bigint,
	ALTER COLUMN valor_movimentado TYPE numeric(15,4) /* TYPE change - table: outros_movimentos original: numeric(15,2) new: numeric(15,4) */;

ALTER TABLE saldos_estoque_funcionario
	ALTER COLUMN saldo TYPE numeric(15,4) /* TYPE change - table: saldos_estoque_funcionario original: numeric(15,2) new: numeric(15,4) */;

ALTER TABLE saldos_estoque
	ALTER COLUMN saldo TYPE numeric(15,4) /* TYPE change - table: saldos_estoque original: numeric(15,2) new: numeric(15,4) */;

ALTER TABLE movimentos_estoque
	ADD CONSTRAINT movimentos_estoque_id_outros_moviementos_fk FOREIGN KEY (id_outros_movimentos) REFERENCES outros_movimentos(id);

ALTER TABLE outros_movimentos
	ADD CONSTRAINT outros_movimentos_id_operacao_funcionario_fk FOREIGN KEY (id_operacao_funcionario) REFERENCES operacoes_funcionario(id);
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '225','20170612-235018'); 
