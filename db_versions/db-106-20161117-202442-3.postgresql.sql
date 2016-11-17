--liquibase formatted sql
--changeset carloseduardo:1479425095543-1

ALTER TABLE operacoes_diarias_det
	DROP CONSTRAINT ooperacoes_diarias_det_id_funcionario_fkey;

ALTER TABLE operacoes_diarias_det
	DROP CONSTRAINT operacoes_diarias_det_id_operacoes_diarias_fkey;

ALTER TABLE operacoes_diarias_det
	ADD CONSTRAINT operacoes_diarias_det_id_operacao_fkey FOREIGN KEY (id_operacao) REFERENCES operacoes(id);

ALTER TABLE operacoes_diarias_det
	ADD CONSTRAINT operacoes_diarias_det_id_operacoes_diarias_fkey FOREIGN KEY (id_operacoes_diarias) REFERENCES operacoes_diarias(id) ON UPDATE CASCADE ON DELETE CASCADE;
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '106','20161117-202442'); 
