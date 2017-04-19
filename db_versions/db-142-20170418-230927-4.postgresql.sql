--liquibase formatted sql
--changeset carloseduardo:comando_20170418_2317-1
INSERT INTO public.componentes (id, nome_componente, tipo_componemte, observacoes, nome_componente_sistema, nome_coluna_id_componente) VALUES (1, 'Cofre', 1, 'Cofre', 'cofres', 'id_cofre');