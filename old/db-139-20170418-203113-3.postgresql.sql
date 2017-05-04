--liquibase formatted sql
--changeset carloseduardo:1492558293085-1

ALTER TABLE componentes
	ALTER COLUMN nome_coluna_id_componente SET DEFAULT NULL::character varying;
INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('GestorLotericoWeb', '1.0-SNAPSHOT', '139','20170418-203113'); 
