--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

-- Started on 2016-08-09 23:17:05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 195 (class 1259 OID 16620)
-- Name: cols_descri; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cols_descri (
    id bigint DEFAULT nextval('seq_cols_descri'::regclass) NOT NULL,
    coluna character varying(200) NOT NULL,
    descricao character varying(1000) NOT NULL
);


ALTER TABLE cols_descri OWNER TO postgres;

--
-- TOC entry 2177 (class 0 OID 16620)
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
17	nome_cofre	Nome do Cofre
18	tipo_cofre	Tipo Cofre
\.


--
-- TOC entry 2062 (class 2606 OID 16628)
-- Name: cols_descri_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cols_descri
    ADD CONSTRAINT cols_descri_pkey PRIMARY KEY (id);


-- Completed on 2016-08-09 23:17:05

--
-- PostgreSQL database dump complete
--

