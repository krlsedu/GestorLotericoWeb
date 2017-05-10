--liquibase formatted sql
--changeset carloseduardo:comando_ajuste_20170507-1

UPDATE saldos_cofres SET valor_movimentado = (SELECT valor_movimentado FROM movimentos_cofres WHERE movimentos_cofres.id = saldos_cofres.id_movimento_cofre and saldos_cofres.valor_movimentado is null);