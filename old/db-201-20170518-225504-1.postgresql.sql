--liquibase formatted sql

--changeset CarlosEduardo:1495158912218-1
ALTER TABLE movimentos_contas ADD id_loterica BIGINT;

--changeset CarlosEduardo:1495158912218-2
ALTER TABLE movimentos_contas ADD CONSTRAINT movimentos_contas_id_loterica__fk FOREIGN KEY (id_loterica) REFERENCES lotericas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

