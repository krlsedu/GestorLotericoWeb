--liquibase formatted sql

--changeset CarlosEduardo:1472602832735-1
CREATE TABLE movimentos_cofres (id BIGSERIAL NOT NULL, id_cofre BIGINT NOT NULL, id_movimento_caixa BIGINT, data_hora_mov TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, data_estorno date, valor_movimentado numeric(15, 2) DEFAULT 0 NOT NULL, tipo_movimento VARCHAR(1) NOT NULL, observacoes TEXT, data_cadastro TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, id_entidade BIGINT NOT NULL, CONSTRAINT movimentos_cofres_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1472602832735-2
CREATE TABLE saldos_cofres (id BIGSERIAL NOT NULL, id_cofre BIGINT NOT NULL, data_saldo date DEFAULT NOW() NOT NULL, saldo numeric(15, 2) DEFAULT 0 NOT NULL, CONSTRAINT saldos_cofres_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1472602832735-3
ALTER TABLE movimentos_caixas ADD id_cofre BIGINT;

--changeset CarlosEduardo:1472602832735-4
ALTER TABLE movimentos_cofres ADD CONSTRAINT mmovimentos_cofre_id_cofre_fkey FOREIGN KEY (id_cofre) REFERENCES cofres (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1472602832735-5
ALTER TABLE movimentos_cofres ADD CONSTRAINT mmovimentos_cofre_id_movimento_caixa_fkey FOREIGN KEY (id_movimento_caixa) REFERENCES movimentos_caixas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1472602832735-6
ALTER TABLE movimentos_caixas ADD CONSTRAINT movimentos_caixas_id_cofre_fkey FOREIGN KEY (id_cofre) REFERENCES cofres (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1472602832735-7
ALTER TABLE movimentos_cofres ADD CONSTRAINT movimentos_cofres_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1472602832735-8
ALTER TABLE saldos_cofres ADD CONSTRAINT saldos_cofres_id_cofre_fkey FOREIGN KEY (id_cofre) REFERENCES cofres (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

