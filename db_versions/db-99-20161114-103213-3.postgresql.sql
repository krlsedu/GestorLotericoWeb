--liquibase formatted sql
--changeset carloseduardo:1479130344173-1

ALTER TABLE saldos_cofres
	DROP CONSTRAINT saldos_cofres_id_movimentos_cofres;

ALTER TABLE saldos_cofres
	ADD CONSTRAINT saldos_cofres_id_movimentos_cofres FOREIGN KEY (id_movimento_cofre) REFERENCES movimentos_cofres(id) ON UPDATE CASCADE ON DELETE CASCADE;
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '99','20161114-103213'); 
