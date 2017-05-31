--liquibase formatted sql

--changeset CarlosEduardo:1471915322671-1
ALTER TABLE abertura_terminais DROP CONSTRAINT aberturas_terminais_id_loterica_fkey;

--changeset CarlosEduardo:1471915322671-2
ALTER TABLE abertura_terminais DROP COLUMN id_loterica;

