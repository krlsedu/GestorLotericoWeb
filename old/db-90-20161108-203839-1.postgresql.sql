--liquibase formatted sql

--changeset CarlosEduardo:1478648331357-1
CREATE TABLE movimentos_contas (id BIGSERIAL NOT NULL, id_conta BIGINT NOT NULL, data_hora_mov TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, data_estorno date, valor_movimentado numeric(15, 2) DEFAULT 0 NOT NULL, tipo_movimento_conta VARCHAR(1) NOT NULL, forma_deposito INT NOT NULL, observacoes TEXT, data_cadastro TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, id_entidade BIGINT NOT NULL, id_cofre BIGINT, CONSTRAINT movimentos_contas_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1478648331357-2
ALTER TABLE movimentos_cofres ADD id_movimento_conta BIGINT;

--changeset CarlosEduardo:1478648331357-3
ALTER TABLE movimentos_cofres ADD CONSTRAINT mmovimentos_cofre_id_movimento_conta_fkey FOREIGN KEY (id_movimento_conta) REFERENCES movimentos_contas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1478648331357-4
ALTER TABLE movimentos_contas ADD CONSTRAINT mmovimentos_contas_id_conta_fkey FOREIGN KEY (id_conta) REFERENCES contas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1478648331357-5
ALTER TABLE movimentos_contas ADD CONSTRAINT movimentos_contas_id_cofre_fk FOREIGN KEY (id_cofre) REFERENCES cofres (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1478648331357-6
ALTER TABLE movimentos_contas ADD CONSTRAINT movimentos_contass_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

