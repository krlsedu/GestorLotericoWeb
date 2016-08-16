--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

-- Started on 2016-08-15 21:41:52

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 2191 (class 0 OID 16408)
-- Dependencies: 183
-- Data for Name: entidades; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY entidades (id, nome, tipo) FROM stdin;
2	Testes	1
\.


--
-- TOC entry 2192 (class 0 OID 16589)
-- Dependencies: 193
-- Data for Name: usuarios_entidades; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY usuarios_entidades (id, id_usuario, id_entidade) FROM stdin;
1	1	2
\.


-- Completed on 2016-08-15 21:41:52

--
-- PostgreSQL database dump complete
--

