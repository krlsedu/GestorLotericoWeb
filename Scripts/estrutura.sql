--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

-- Started on 2016-08-04 11:34:46

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
-- TOC entry 2177 (class 0 OID 0)
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
-- TOC entry 2178 (class 0 OID 0)
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
    nome character varying NOT NULL,
    tipo smallint DEFAULT 1 NOT NULL
);


ALTER TABLE entidades OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 16474)
-- Name: login_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE login_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE login_seq OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 16476)
-- Name: logins; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE logins (
    id bigint DEFAULT nextval('login_seq'::regclass) NOT NULL,
    usuario character(50) NOT NULL,
    ip character(200) NOT NULL,
    nome_maquina character(200) NOT NULL,
    sucesso character(1) DEFAULT 'S'::bpchar,
    datatentativa timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE logins OWNER TO postgres;

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
-- TOC entry 191 (class 1259 OID 16516)
-- Name: lotericas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lotericas (
    id bigint DEFAULT nextval('seq_lotericas'::regclass) NOT NULL,
    codigo_caixa character varying(200) NOT NULL,
    nome character varying(200) NOT NULL,
    id_entidade bigint DEFAULT 2 NOT NULL
);


ALTER TABLE lotericas OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 16427)
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
-- TOC entry 192 (class 1259 OID 16564)
-- Name: seq_usuarios_entidades; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_usuarios_entidades
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_usuarios_entidades OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 16484)
-- Name: sessoes_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sessoes_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sessoes_seq OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 16500)
-- Name: sessoes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE sessoes (
    id bigint DEFAULT nextval('sessoes_seq'::regclass) NOT NULL,
    id_usuario bigint NOT NULL,
    id_entidade bigint NOT NULL,
    id_sessao character(200) NOT NULL,
    ip character(200) NOT NULL,
    nome_maquina character(200) NOT NULL,
    pg_anterior text,
    pg_atual text,
    tempo_sessao smallint DEFAULT 0 NOT NULL
);


ALTER TABLE sessoes OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 16462)
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuarios (
    id bigint DEFAULT nextval('seq_users'::regclass) NOT NULL,
    codigo_caixa bigint,
    usuario character(50) NOT NULL,
    senha chkpass NOT NULL,
    tipo smallint DEFAULT 1 NOT NULL,
    nome character(200) NOT NULL,
    email character(200) NOT NULL,
    telefone character(50)
);


ALTER TABLE usuarios OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 16589)
-- Name: usuarios_entidades; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuarios_entidades (
    id bigint DEFAULT nextval('seq_usuarios_entidades'::regclass) NOT NULL,
    id_usuario bigint NOT NULL,
    id_entidade bigint NOT NULL
);


ALTER TABLE usuarios_entidades OWNER TO postgres;

--
-- TOC entry 2038 (class 2606 OID 16523)
-- Name: entidades_nome_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entidades
    ADD CONSTRAINT entidades_nome_key UNIQUE (nome);


--
-- TOC entry 2040 (class 2606 OID 16414)
-- Name: entidades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entidades
    ADD CONSTRAINT entidades_pkey PRIMARY KEY (id);


--
-- TOC entry 2046 (class 2606 OID 16483)
-- Name: logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY logins
    ADD CONSTRAINT logins_pkey PRIMARY KEY (id);


--
-- TOC entry 2050 (class 2606 OID 16521)
-- Name: lotericas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lotericas
    ADD CONSTRAINT lotericas_pkey PRIMARY KEY (id);


--
-- TOC entry 2048 (class 2606 OID 16509)
-- Name: sessoes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sessoes
    ADD CONSTRAINT sessoes_pkey PRIMARY KEY (id);


--
-- TOC entry 2042 (class 2606 OID 16471)
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2044 (class 2606 OID 16473)
-- Name: users_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT users_usuario_key UNIQUE (usuario);


--
-- TOC entry 2052 (class 2606 OID 16594)
-- Name: usuarios_entidades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_entidades
    ADD CONSTRAINT usuarios_entidades_pkey PRIMARY KEY (id);


--
-- TOC entry 2053 (class 2606 OID 16559)
-- Name: lotericas_i_entidades_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lotericas
    ADD CONSTRAINT lotericas_i_entidades_fkey FOREIGN KEY (id_entidade) REFERENCES entidades(id);


--
-- TOC entry 2054 (class 2606 OID 16595)
-- Name: usuarios_entidades_i_entidades_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_entidades
    ADD CONSTRAINT usuarios_entidades_i_entidades_fkey FOREIGN KEY (id_entidade) REFERENCES entidades(id);


--
-- TOC entry 2055 (class 2606 OID 16600)
-- Name: usuarios_entidades_i_usuarios_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_entidades
    ADD CONSTRAINT usuarios_entidades_i_usuarios_fkey FOREIGN KEY (id_usuario) REFERENCES usuarios(id);


--
-- TOC entry 2176 (class 0 OID 0)
-- Dependencies: 7
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-08-04 11:34:46

--
-- PostgreSQL database dump complete
--

