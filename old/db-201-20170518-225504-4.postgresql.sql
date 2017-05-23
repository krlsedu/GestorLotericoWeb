--liquibase formatted sql
--changeset carloseduardo:comando_personalizado-20170518-1
UPDATE movimentos_contas set id_loterica = (SELECT id from lotericas where lotericas.id_entidade = movimentos_contas.id_entidade) where id_loterica is NULL;
INSERT INTO public.componentes (id, nome_componente, tipo_componemte, observacoes, nome_componente_sistema, nome_coluna_id_componente) VALUES (2, 'Saldo geral conta Lotérica', 2, '', 'lotericas', 'id_loterica');