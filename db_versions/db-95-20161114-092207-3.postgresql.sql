--liquibase formatted sql
--changeset carloseduardo:1479126139095-1

ALTER TABLE saldos_cofres
	ALTER COLUMN data_hora_movimento TYPE timestamp without time zone /* TYPE change - table: saldos_cofres original: timestamp(6) without time zone new: timestamp without time zone */;
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '95','20161114-092207'); 
