--liquibase formatted sql

--changeset CarlosEduardo:1495162081465-1
ALTER TABLE entidades ADD codificacao VARCHAR(200) DEFAULT 'ENG-US' NOT NULL;

