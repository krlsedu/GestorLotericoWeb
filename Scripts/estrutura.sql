--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

-- Started on 2016-08-09 23:17:17

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
-- TOC entry 2259 (class 0 OID 0)
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
-- TOC entry 2260 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION chkpass; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION chkpass IS 'data type for auto-encrypted passwords';


SET search_path = public, pg_catalog;

--
-- TOC entry 206 (class 1259 OID 16806)
-- Name: seq_cofres; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_cofres
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_cofres OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 207 (class 1259 OID 16808)
-- Name: cofres; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cofres (
    id bigint DEFAULT nextval('seq_cofres'::regclass) NOT NULL,
    nome_cofre character varying(200) NOT NULL,
    tipo_cofre smallint NOT NULL,
    observacoes text,
    id_loterica bigint NOT NULL,
    id_entidade bigint NOT NULL
);


ALTER TABLE cofres OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 16608)
-- Name: seq_cols_descri; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_cols_descri
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_cols_descri OWNER TO postgres;

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
-- TOC entry 202 (class 1259 OID 16747)
-- Name: seq_contas; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_contas
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_contas OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 16768)
-- Name: contas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE contas (
    id bigint DEFAULT nextval('seq_contas'::regclass) NOT NULL,
    conta_corrente character varying(200) NOT NULL,
    dv character varying(20) NOT NULL,
    nome_conta character varying(200) NOT NULL,
    operacao character varying(200) NOT NULL,
    agencia character varying(200) NOT NULL,
    telefone character varying(200) NOT NULL,
    gerente character varying(200) NOT NULL,
    id_loterica bigint NOT NULL,
    observacoes text,
    id_entidade bigint NOT NULL
);


ALTER TABLE contas OWNER TO postgres;

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
-- TOC entry 198 (class 1259 OID 16686)
-- Name: seq_erros; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_erros
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_erros OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 16688)
-- Name: erros; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE erros (
    id bigint DEFAULT nextval('seq_erros'::regclass) NOT NULL,
    id_usuario bigint NOT NULL,
    id_entidade bigint NOT NULL,
    id_sessao character(200) NOT NULL,
    ip character(200) NOT NULL,
    nome_maquina character(200) NOT NULL,
    pg_anterior text,
    pg_atual text,
    data_erro timestamp without time zone DEFAULT now() NOT NULL,
    erro text,
    stacktrace text
);


ALTER TABLE erros OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16706)
-- Name: seq_funcionarios; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_funcionarios
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_funcionarios OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 16732)
-- Name: funcionarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE funcionarios (
    id bigint DEFAULT nextval('seq_funcionarios'::regclass) NOT NULL,
    codigo_caixa character varying(200) NOT NULL,
    nome character varying(200) NOT NULL,
    cpf character varying(20) NOT NULL,
    tipo_func smallint DEFAULT 1 NOT NULL,
    id_entidade bigint NOT NULL,
    observacoes text
);


ALTER TABLE funcionarios OWNER TO postgres;

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
-- TOC entry 204 (class 1259 OID 16787)
-- Name: seq_operacoes; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_operacoes
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_operacoes OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16789)
-- Name: operacoes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE operacoes (
    id bigint DEFAULT nextval('seq_operacoes'::regclass) NOT NULL,
    nome_oper_caixa character varying(200) NOT NULL,
    nome_oper character varying(200) NOT NULL,
    tipo_oper smallint NOT NULL,
    observacoes text,
    id_entidade bigint DEFAULT 2 NOT NULL
);


ALTER TABLE operacoes OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 16649)
-- Name: seq_terminais; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_terminais
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_terminais OWNER TO postgres;

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
    tempo_sessao smallint DEFAULT 0 NOT NULL,
    data_acesso timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE sessoes OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16665)
-- Name: terminais; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE terminais (
    id bigint DEFAULT nextval('seq_terminais'::regclass) NOT NULL,
    codigo_caixa character varying(200) NOT NULL,
    nome character varying(200) NOT NULL,
    marca character varying(200),
    modelo character varying(200),
    observacoes text,
    id_loterica bigint NOT NULL,
    id_entidade bigint NOT NULL
);


ALTER TABLE terminais OWNER TO postgres;

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
-- TOC entry 2126 (class 2606 OID 16816)
-- Name: cofres_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cofres
    ADD CONSTRAINT cofres_pkey PRIMARY KEY (id);


--
-- TOC entry 2114 (class 2606 OID 16628)
-- Name: cols_descri_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cols_descri
    ADD CONSTRAINT cols_descri_pkey PRIMARY KEY (id);


--
-- TOC entry 2122 (class 2606 OID 16776)
-- Name: contas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contas
    ADD CONSTRAINT contas_pkey PRIMARY KEY (id);


--
-- TOC entry 2098 (class 2606 OID 16523)
-- Name: entidades_nome_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entidades
    ADD CONSTRAINT entidades_nome_key UNIQUE (nome);


--
-- TOC entry 2100 (class 2606 OID 16414)
-- Name: entidades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entidades
    ADD CONSTRAINT entidades_pkey PRIMARY KEY (id);


--
-- TOC entry 2118 (class 2606 OID 16697)
-- Name: erros_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY erros
    ADD CONSTRAINT erros_pkey PRIMARY KEY (id);


--
-- TOC entry 2120 (class 2606 OID 16741)
-- Name: funcionarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY funcionarios
    ADD CONSTRAINT funcionarios_pkey PRIMARY KEY (id);


--
-- TOC entry 2106 (class 2606 OID 16483)
-- Name: logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY logins
    ADD CONSTRAINT logins_pkey PRIMARY KEY (id);


--
-- TOC entry 2110 (class 2606 OID 16521)
-- Name: lotericas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lotericas
    ADD CONSTRAINT lotericas_pkey PRIMARY KEY (id);


--
-- TOC entry 2124 (class 2606 OID 16798)
-- Name: operacoes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY operacoes
    ADD CONSTRAINT operacoes_pkey PRIMARY KEY (id);


--
-- TOC entry 2108 (class 2606 OID 16509)
-- Name: sessoes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sessoes
    ADD CONSTRAINT sessoes_pkey PRIMARY KEY (id);


--
-- TOC entry 2116 (class 2606 OID 16673)
-- Name: terminais_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY terminais
    ADD CONSTRAINT terminais_pkey PRIMARY KEY (id);


--
-- TOC entry 2102 (class 2606 OID 16471)
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2104 (class 2606 OID 16473)
-- Name: users_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT users_usuario_key UNIQUE (usuario);


--
-- TOC entry 2112 (class 2606 OID 16594)
-- Name: usuarios_entidades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_entidades
    ADD CONSTRAINT usuarios_entidades_pkey PRIMARY KEY (id);


--
-- TOC entry 2136 (class 2606 OID 16817)
-- Name: cofres_id_entidade_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cofres
    ADD CONSTRAINT cofres_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades(id);


--
-- TOC entry 2137 (class 2606 OID 16822)
-- Name: cofres_id_loterica_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cofres
    ADD CONSTRAINT cofres_id_loterica_fkey FOREIGN KEY (id_loterica) REFERENCES lotericas(id);


--
-- TOC entry 2133 (class 2606 OID 16777)
-- Name: contas_id_entidade_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contas
    ADD CONSTRAINT contas_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades(id);


--
-- TOC entry 2134 (class 2606 OID 16782)
-- Name: contas_id_loterica_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contas
    ADD CONSTRAINT contas_id_loterica_fkey FOREIGN KEY (id_loterica) REFERENCES lotericas(id);


--
-- TOC entry 2132 (class 2606 OID 16742)
-- Name: funcionarios_id_loterica_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY funcionarios
    ADD CONSTRAINT funcionarios_id_loterica_fkey FOREIGN KEY (id_entidade) REFERENCES entidades(id);


--
-- TOC entry 2127 (class 2606 OID 16559)
-- Name: lotericas_i_entidades_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lotericas
    ADD CONSTRAINT lotericas_i_entidades_fkey FOREIGN KEY (id_entidade) REFERENCES entidades(id);


--
-- TOC entry 2135 (class 2606 OID 16799)
-- Name: operacoes_i_entidades_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY operacoes
    ADD CONSTRAINT operacoes_i_entidades_fkey FOREIGN KEY (id_entidade) REFERENCES entidades(id);


--
-- TOC entry 2131 (class 2606 OID 16679)
-- Name: terminais_id_entidade_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY terminais
    ADD CONSTRAINT terminais_id_entidade_fkey FOREIGN KEY (id_entidade) REFERENCES entidades(id);


--
-- TOC entry 2130 (class 2606 OID 16674)
-- Name: terminais_id_loterica_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY terminais
    ADD CONSTRAINT terminais_id_loterica_fkey FOREIGN KEY (id_loterica) REFERENCES lotericas(id);


--
-- TOC entry 2128 (class 2606 OID 16595)
-- Name: usuarios_entidades_i_entidades_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_entidades
    ADD CONSTRAINT usuarios_entidades_i_entidades_fkey FOREIGN KEY (id_entidade) REFERENCES entidades(id);


--
-- TOC entry 2129 (class 2606 OID 16600)
-- Name: usuarios_entidades_i_usuarios_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_entidades
    ADD CONSTRAINT usuarios_entidades_i_usuarios_fkey FOREIGN KEY (id_usuario) REFERENCES usuarios(id);


--
-- TOC entry 2258 (class 0 OID 0)
-- Dependencies: 7
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-08-09 23:17:18

--
-- PostgreSQL database dump complete
--

