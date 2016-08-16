--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

-- Started on 2016-08-15 21:42:40

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 2196 (class 0 OID 16620)
-- Dependencies: 195
-- Data for Name: cols_descri; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO cols_descri VALUES (1, 'id', 'Código');
INSERT INTO cols_descri VALUES (2, 'codigo_caixa', 'Código na Caixa');
INSERT INTO cols_descri VALUES (3, 'nome', 'Nome');
INSERT INTO cols_descri VALUES (4, 'id_loterica', 'Código da lotérica (Sistema)');
INSERT INTO cols_descri VALUES (5, 'marca', 'Marca');
INSERT INTO cols_descri VALUES (6, 'modelo', 'Modelo');
INSERT INTO cols_descri VALUES (7, 'observacoes', 'Observações');
INSERT INTO cols_descri VALUES (8, 'cpf', 'CPF');
INSERT INTO cols_descri VALUES (9, 'tipo', 'Tipo de Cadastro');
INSERT INTO cols_descri VALUES (10, 'tipo_func', 'Tipo Funcionário');
INSERT INTO cols_descri VALUES (11, 'nome_conta', 'Nome da conta');
INSERT INTO cols_descri VALUES (12, 'conta_corrente', 'Número da Conta');
INSERT INTO cols_descri VALUES (13, 'operacao', 'Operação');
INSERT INTO cols_descri VALUES (14, 'nome_oper_caixa', 'Nome da Operação (Caixa)');
INSERT INTO cols_descri VALUES (15, 'nome_oper', 'Nome da Operação');
INSERT INTO cols_descri VALUES (16, 'tipo_oper', 'Tipo Operação');
INSERT INTO cols_descri VALUES (17, 'nome_cofre', 'Nome do Cofre');
INSERT INTO cols_descri VALUES (18, 'tipo_cofre', 'Tipo Cofre');


--
-- TOC entry 2194 (class 0 OID 16408)
-- Dependencies: 183
-- Data for Name: entidades; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO entidades VALUES (2, 'Testes', 1);


--
-- TOC entry 2195 (class 0 OID 16589)
-- Dependencies: 193
-- Data for Name: usuarios_entidades; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO usuarios_entidades VALUES (1, 1, 2);


-- Completed on 2016-08-15 21:42:41

--
-- PostgreSQL database dump complete
--

