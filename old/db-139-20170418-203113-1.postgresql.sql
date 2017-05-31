--liquibase formatted sql

--changeset CarlosEduardo:1492558288143-1
ALTER TABLE componentes ADD nome_componente_sistema VARCHAR(200) NOT NULL;

--changeset CarlosEduardo:1492558288143-2
ALTER TABLE componentes ADD nome_coluna_id_componente VARCHAR(200) DEFAULT 'NULL::character varying';

