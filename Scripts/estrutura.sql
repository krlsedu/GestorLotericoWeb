--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

-- Started on 2016-08-02 17:15:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12355)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2141 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 2 (class 3079 OID 16452)
-- Name: chkpass; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS chkpass WITH SCHEMA public;


--
-- TOC entry 2142 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION chkpass; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION chkpass IS 'data type for auto-encrypted passwords';


SET search_path = public, pg_catalog;

--
-- TOC entry 182 (class 1259 OID 16394)
-- Name: seq_entidades; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_entidades
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_entidades OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 183 (class 1259 OID 16408)
-- Name: entidades; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE entidades (
    id bigint DEFAULT nextval('seq_entidades'::regclass) NOT NULL,
    nome character(200) NOT NULL,
    tipo smallint DEFAULT 1 NOT NULL
);


ALTER TABLE entidades OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 16417)
-- Name: seq_lotericas; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_lotericas
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_lotericas OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 16419)
-- Name: lotericas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lotericas (
    id bigint DEFAULT nextval('seq_lotericas'::regclass) NOT NULL,
    codigo_caixa bigint,
    nome character(200) NOT NULL
);


ALTER TABLE lotericas OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 16427)
-- Name: seq_users; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_users
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_users OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 16462)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    id bigint DEFAULT nextval('seq_users'::regclass) NOT NULL,
    codigo_caixa bigint,
    usuario character(50) NOT NULL,
    senha chkpass NOT NULL,
    tipo smallint DEFAULT 1 NOT NULL,
    nome character(200) NOT NULL,
    email character(200) NOT NULL,
    telefone character(50)
);


ALTER TABLE users OWNER TO postgres;

--
-- TOC entry 2011 (class 2606 OID 16416)
-- Name: entidades_nome_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entidades
    ADD CONSTRAINT entidades_nome_key UNIQUE (nome);


--
-- TOC entry 2013 (class 2606 OID 16414)
-- Name: entidades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entidades
    ADD CONSTRAINT entidades_pkey PRIMARY KEY (id);


--
-- TOC entry 2015 (class 2606 OID 16424)
-- Name: lotericas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lotericas
    ADD CONSTRAINT lotericas_pkey PRIMARY KEY (id);


--
-- TOC entry 2017 (class 2606 OID 16471)
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2019 (class 2606 OID 16473)
-- Name: users_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_usuario_key UNIQUE (usuario);


--
-- TOC entry 2140 (class 0 OID 0)
-- Dependencies: 7
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-08-02 17:15:11

--
-- PostgreSQL database dump complete
--

