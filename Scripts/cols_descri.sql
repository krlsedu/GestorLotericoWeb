--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

-- Started on 2016-08-09 21:54:56

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 2171 (class 0 OID 16620)
-- Dependencies: 195
-- Data for Name: cols_descri; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cols_descri (id, coluna, descricao) FROM stdin;
1	id	Código
2	codigo_caixa	Código na Caixa
3	nome	Nome
4	id_loterica	Código da lotérica (Sistema)
5	marca	Marca
6	modelo	Modelo
7	observacoes	Observações
8	cpf	CPF
9	tipo	Tipo de Cadastro
10	tipo_func	Tipo Funcionário
11	nome_conta	Nome da conta
12	conta_corrente	Número da Conta
13	operacao	Operação
14	nome_oper_caixa	Nome da Operação (Caixa)
15	nome_oper	Nome da Operação
16	tipo_oper	Tipo Operação
\.


-- Completed on 2016-08-09 21:54:56

--
-- PostgreSQL database dump complete
--

