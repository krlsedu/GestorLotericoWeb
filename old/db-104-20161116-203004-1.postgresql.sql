--liquibase formatted sql

--changeset CarlosEduardo:1479339014779-1
CREATE TABLE operacoes_diarias (id BIGSERIAL NOT NULL, id_terminal BIGINT NOT NULL, id_funcionario BIGINT NOT NULL, data_operacoes date DEFAULT NOW() NOT NULL, data_hora_mov TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, data_estorno date, observacoes TEXT, id_entidade BIGINT NOT NULL, CONSTRAINT operacoes_diarias_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1479339014779-2
CREATE TABLE operacoes_diarias_det (id BIGSERIAL NOT NULL, id_operacoes_diarias BIGINT NOT NULL, id_operacao BIGINT NOT NULL, quantidade BIGINT NOT NULL, CONSTRAINT operacoes_diarias_dets_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1479339014779-3
ALTER TABLE operacoes_diarias_det ADD CONSTRAINT ooperacoes_diarias_det_id_funcionario_fkey FOREIGN KEY (id_operacao) REFERENCES operacoes (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1479339014779-4
ALTER TABLE operacoes_diarias_det ADD CONSTRAINT operacoes_diarias_det_id_operacoes_diarias_fkey FOREIGN KEY (id_operacoes_diarias) REFERENCES operacoes_diarias (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1479339014779-5
ALTER TABLE operacoes_diarias ADD CONSTRAINT operacoes_diarias_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1479339014779-6
ALTER TABLE operacoes_diarias ADD CONSTRAINT operacoes_diarias_id_funcionario_fkey FOREIGN KEY (id_funcionario) REFERENCES funcionarios (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1479339014779-7
ALTER TABLE operacoes_diarias ADD CONSTRAINT operacoes_diarias_id_terminal_fkey FOREIGN KEY (id_terminal) REFERENCES terminais (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

