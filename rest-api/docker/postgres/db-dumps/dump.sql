--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.3
-- Dumped by pg_dump version 9.6.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;




CREATE TABLE user (
    id uuid NOT NULL,
    version integer NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    name text NOT NULL
);
ALTER TABLE user OWNER TO app_rw;
ALTER TABLE ONLY user
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);

CREATE TABLE job (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    version integer NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    title character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    price numeric(15,2) NOT NULL
);
ALTER TABLE job OWNER TO app_rw;
ALTER TABLE ONLY job
    ADD CONSTRAINT job_pkey PRIMARY KEY (id);
ALTER TABLE ONLY job
    ADD CONSTRAINT job_user_id_fkey FOREIGN KEY (user_id) REFERENCES user(id);
--
-- PostgreSQL database dump complete
--

