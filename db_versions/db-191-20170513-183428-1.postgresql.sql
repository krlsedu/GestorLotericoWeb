--liquibase formatted sql

--changeset CarlosEduardo:1494711278473-1
ALTER TABLE fechamento_terminais ADD id_loterica BIGINT;

--changeset CarlosEduardo:1494711278473-2
ALTER TABLE fechamento_terminais ADD CONSTRAINT fechamento_terminais_id_lotericas_fk FOREIGN KEY (id_loterica) REFERENCES lotericas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

