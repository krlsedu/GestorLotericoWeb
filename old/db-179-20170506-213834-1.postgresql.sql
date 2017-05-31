--liquibase formatted sql

--changeset CarlosEduardo:1494117523558-1
ALTER TABLE saldos_cofres ADD valor_movimentado numeric(15, 2) DEFAULT 0;

