--liquibase formatted sql

--changeset CarlosEduardo:1492263618999-1
CREATE TABLE componentes (id BIGSERIAL NOT NULL, nome_componente VARCHAR(200) NOT NULL, tipo_componemte SMALLINT NOT NULL, observacoes TEXT, CONSTRAINT componentes_pkey PRIMARY KEY (id));

