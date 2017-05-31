--liquibase formatted sql

--changeset CarlosEduardo:1479126135666-1
ALTER TABLE saldos_cofres ADD id_entidade BIGINT DEFAULT 1 NOT NULL;

--changeset CarlosEduardo:1479126135666-2
ALTER TABLE saldos_cofres ADD id_movimento_cofre BIGINT;

--changeset CarlosEduardo:1479126135666-3
ALTER TABLE saldos_cofres ADD data_hora_movimento TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT NOW() NOT NULL;

--changeset CarlosEduardo:1479126135666-4
ALTER TABLE saldos_cofres ADD CONSTRAINT saldos_cofres_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1479126135666-5
ALTER TABLE saldos_cofres ADD CONSTRAINT saldos_cofres_id_movimentos_cofres FOREIGN KEY (id_movimento_cofre) REFERENCES movimentos_cofres (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

