--liquibase formatted sql

--changeset CarlosEduardo:1493862055037-1
ALTER TABLE abertura_terminais ADD id_cofe BIGINT;

--changeset CarlosEduardo:1493862055037-2
ALTER TABLE movimentos_cofres ADD id_abertura_terminal BIGINT;

--changeset CarlosEduardo:1493862055037-3
ALTER TABLE abertura_terminais ADD CONSTRAINT abertura_terminais_id_cofre_fk FOREIGN KEY (id_cofre) REFERENCES cofres (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1493862055037-4
ALTER TABLE movimentos_cofres ADD CONSTRAINT movimentos_cofres_id_abertura_terminal_fk FOREIGN KEY (id_abertura_terminal) REFERENCES abertura_terminais (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

