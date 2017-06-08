--liquibase formatted sql
--changeset carloseduardo:1496533103814-1

ALTER TABLE movimentos_caixas
	ADD COLUMN valor_moeda numeric(15,2);

ALTER TABLE movimentos_estoque
	ADD COLUMN id_movimento_caixa bigint;

ALTER TABLE movimentos_estoque
	ADD CONSTRAINT movimentos_estoque_id_movimento_caixa_fk FOREIGN KEY (id_movimento_caixa) REFERENCES movimentos_caixas(id);
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '223','20170603-203819'); 
