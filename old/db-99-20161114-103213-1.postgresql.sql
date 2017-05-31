--liquibase formatted sql

--changeset CarlosEduardo:1479130340358-1
ALTER TABLE saldos_cofres DROP CONSTRAINT saldos_cofres_id_movimentos_cofres;
ALTER TABLE saldos_cofres ADD CONSTRAINT saldos_cofres_id_movimentos_cofres FOREIGN KEY (id_movimento_cofre) REFERENCES movimentos_cofres (id);

