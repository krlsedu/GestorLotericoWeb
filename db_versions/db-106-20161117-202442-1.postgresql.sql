--liquibase formatted sql

--changeset CarlosEduardo:1479425091838-1
ALTER TABLE operacoes_diarias_det DROP CONSTRAINT operacoes_diarias_det_id_operacoes_diarias_fkey;
ALTER TABLE operacoes_diarias_det ADD CONSTRAINT operacoes_diarias_det_id_operacoes_diarias_fkey FOREIGN KEY (id_operacoes_diarias) REFERENCES operacoes_diarias (id);

