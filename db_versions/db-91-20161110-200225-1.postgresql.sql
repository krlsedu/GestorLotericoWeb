--liquibase formatted sql

--changeset CarlosEduardo:1478818956304-1
ALTER TABLE movimentos_contas ADD numero_volumes BIGINT DEFAULT 1 NOT NULL;

