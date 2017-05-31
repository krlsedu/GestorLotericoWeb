--liquibase formatted sql

--changeset CarlosEduardo:1473722435903-1
ALTER TABLE movimentos_cofres ADD tipo_movimento_cofre VARCHAR(1) NOT NULL;

--changeset CarlosEduardo:1473722435903-2
ALTER TABLE movimentos_cofres DROP COLUMN tipo_movimento;

