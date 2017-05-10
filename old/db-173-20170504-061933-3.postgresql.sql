--liquibase formatted sql
--changeset carloseduardo:1493889578364-1

ALTER TABLE abertura_terminais
	DROP CONSTRAINT abertura_terminais_id_cofre_fk;

ALTER TABLE abertura_terminais
	DROP COLUMN id_cofe,
	ADD COLUMN id_cofre bigint;

ALTER TABLE abertura_terminais
	ADD CONSTRAINT abertura_terminais_id_cofre_fk FOREIGN KEY (id_cofre) REFERENCES cofres(id);
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '173','20170504-061933'); 
