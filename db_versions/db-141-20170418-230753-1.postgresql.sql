--liquibase formatted sql

--changeset CarlosEduardo:1492567680166-1
CREATE TABLE itens_estatisticas (id BIGSERIAL NOT NULL, linha INT NOT NULL, coluna INT NOT NULL, id_componente BIGINT NOT NULL, id_item_componente BIGINT NOT NULL, estilo_widget TEXT, nome_widget VARCHAR(200) NOT NULL, id_loterica BIGINT NOT NULL, id_entidade BIGINT NOT NULL, tipo_widget INT NOT NULL, html TEXT, CONSTRAINT itens_estatisticas_entidades_pkey PRIMARY KEY (id));

--changeset CarlosEduardo:1492567680166-2
ALTER TABLE itens_estatisticas ADD CONSTRAINT itens_estatisticas_i_entidades_fkey FOREIGN KEY (id_entidade) REFERENCES entidades (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1492567680166-3
ALTER TABLE itens_estatisticas ADD CONSTRAINT itens_estatisticas_id_componente_fkey FOREIGN KEY (id_componente) REFERENCES componentes (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset CarlosEduardo:1492567680166-4
ALTER TABLE itens_estatisticas ADD CONSTRAINT itens_estatisticas_id_loterica_fkey FOREIGN KEY (id_loterica) REFERENCES lotericas (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

