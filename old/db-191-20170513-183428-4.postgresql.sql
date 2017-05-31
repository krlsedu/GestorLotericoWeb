--liquibase formatted sql
--changeset carloseduardo:comando_personalizado-20170513-1
UPDATE fechamento_terminais set id_loterica = (SELECT id from lotericas where lotericas.id_entidade = fechamento_terminais.id_entidade) where id_loterica is NULL ;