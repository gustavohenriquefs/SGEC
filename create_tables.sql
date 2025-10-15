BEGIN;

CREATE TYPE faixa_etaria as ENUM(
	'Livre',
	'10 anos',
	'12 anos',
	'14 anos',
	'16 anos',
	'18 anos'
);

CREATE TYPE service AS ENUM(
	's3'
);

CREATE TABLE IF NOT EXISTS public.colaborador_evento
(
    id_evento integer,
    id_instituicao integer,
    descricao_contribuicao text COLLATE pg_catalog."default" NOT NULL,
    valor_contribuicao character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT vinculo_unico_colaborador_evento UNIQUE (id_instituicao, id_evento)
);

CREATE TABLE IF NOT EXISTS public.colaborador_grupo_eventos
(
    id_instituicao integer,
    id_grupo_eventos integer,
    CONSTRAINT vinculo_unico_colaborador_grupo_eventos UNIQUE (id_instituicao, id_grupo_eventos)
);

CREATE TABLE IF NOT EXISTS public.evento
(
    id_evento serial,
    nome_evento text COLLATE pg_catalog."default" NOT NULL,
    publico_esperado integer,
    publico_alcancado integer,
    descricao text COLLATE pg_catalog."default",
    data_inicial date,
    data_final date,
    horario time without time zone,
    classificacao_etaria faixa_etaria,
    certificavel boolean,
    carga_horaria time without time zone,
    acessivel_em_libras boolean,
    num_participantes_esperado integer,
    num_municipios_esperado integer,
    cadastrado_em date DEFAULT CURRENT_DATE,
    id_grupo_eventos integer,
    num_municipios_alcancado integer DEFAULT 0,
    num_participantes_alcancado integer DEFAULT 0,
    num_colaboradores_alcancado integer DEFAULT 0,
    num_colaboradores_esperado integer,
    id_service_file integer,
    CONSTRAINT evento_pkey PRIMARY KEY (id_evento),
    CONSTRAINT nome_unico UNIQUE (nome_evento)
);

CREATE TABLE IF NOT EXISTS public.grupo_eventos
(
    id_grupo_eventos serial,
    nome_grupo_eventos text COLLATE pg_catalog."default",
    descricao text COLLATE pg_catalog."default",
    classificacao_etaria faixa_etaria,
    publico_esperado integer,
    publico_alcancado integer DEFAULT 0,
    num_acoes_alcancado integer DEFAULT 0,
    num_municipios_alcancado integer DEFAULT 0,
    num_participantes_alcancado integer DEFAULT 0,
    num_acoes_esperado integer,
    num_municipios_esperado integer,
    num_participantes_esperado integer,
    id_service_file integer,
    data_inicial date,
    data_final date,
    num_colaboradores_alcancado integer,
    num_colaboradores_esperado integer DEFAULT 0,
    carga_horaria time without time zone,
    CONSTRAINT grupo_eventos_pkey PRIMARY KEY (id_grupo_eventos),
    CONSTRAINT nome_grupo_eventos_unico UNIQUE (nome_grupo_eventos)
);

CREATE TABLE IF NOT EXISTS public.instituicao
(
    id_instituicao serial,
    nome_instituicao character varying(150) COLLATE pg_catalog."default" NOT NULL,
    id_service_file integer,
    CONSTRAINT instituicao_pkey PRIMARY KEY (id_instituicao),
    CONSTRAINT nome_unico_instituicao UNIQUE (nome_instituicao)
);

CREATE TABLE IF NOT EXISTS public.localizacao
(
    id_localizacao serial,
    estado character varying(30) COLLATE pg_catalog."default" NOT NULL,
    rua character varying(100) COLLATE pg_catalog."default" NOT NULL,
    bairro character varying(100) COLLATE pg_catalog."default",
    numero_rua integer,
    pais character varying(50) COLLATE pg_catalog."default" NOT NULL,
    cidade character varying(50) COLLATE pg_catalog."default" NOT NULL,
    cep character(9) COLLATE pg_catalog."default",
    nome_localizacao character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT localizacao_pkey PRIMARY KEY (id_localizacao)
);

CREATE TABLE IF NOT EXISTS public.localizacao_evento
(
    id_localizacao integer,
    id_evento integer,
    CONSTRAINT par_unico UNIQUE (id_localizacao, id_evento)
);

CREATE TABLE IF NOT EXISTS public.meta
(
    id_meta serial,
    nome_meta character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT meta_pkey PRIMARY KEY (id_meta),
    CONSTRAINT nome_meta_unico UNIQUE (nome_meta)
);

CREATE TABLE IF NOT EXISTS public.meta_evento
(
    id_evento integer,
    id_meta integer,
    CONSTRAINT vinculo_unico_meta_evento UNIQUE (id_meta, id_evento)
);

CREATE TABLE IF NOT EXISTS public.meta_grupo_eventos
(
    id_grupo_eventos integer,
    id_meta integer,
    CONSTRAINT vinculo_unico_meta_grupo_eventos UNIQUE (id_meta, id_grupo_eventos)
);

CREATE TABLE IF NOT EXISTS public.organizador_evento
(
    id_evento integer,
    id_instituicao integer,
    descricao_contribuicao text COLLATE pg_catalog."default" NOT NULL,
    valor_contribuicao character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT vinculo_unico_organizador_evento UNIQUE (id_instituicao, id_evento)
);

CREATE TABLE IF NOT EXISTS public.organizador_grupo_eventos
(
    id_instituicao integer,
    id_grupo_eventos integer,
    CONSTRAINT vinculo_unico_organizador_grupo_eventos UNIQUE (id_instituicao, id_grupo_eventos)
);

CREATE TABLE IF NOT EXISTS public.participante
(
    id_participante serial,
    nome_participante character varying(100) COLLATE pg_catalog."default" NOT NULL,
    area_atuacao character varying(100) COLLATE pg_catalog."default",
    link_perfil text COLLATE pg_catalog."default",
    bio text COLLATE pg_catalog."default",
    id_service_file integer,
    CONSTRAINT participante_pkey PRIMARY KEY (id_participante)
);

CREATE TABLE IF NOT EXISTS public.participante_evento
(
    id_participante integer,
    id_evento integer,
    CONSTRAINT vinculo_unico_participante_evento UNIQUE (id_participante, id_evento)
);

CREATE TABLE IF NOT EXISTS public.service_file
(
    id_service_file serial,
    file_key character varying(256) COLLATE pg_catalog."default",
    suffix character varying(10) COLLATE pg_catalog."default",
    service service,
    bucket character varying(30) COLLATE pg_catalog."default",
    ultima_modificacao date DEFAULT CURRENT_DATE,
    adicionado_em date DEFAULT CURRENT_DATE,
    CONSTRAINT arquivo_pkey PRIMARY KEY (id_service_file)
);

CREATE TABLE IF NOT EXISTS public.service_file_evento
(
    id_evento integer,
    id_service_file integer,
    CONSTRAINT vinculo_unico UNIQUE (id_service_file, id_evento)
);

CREATE TABLE IF NOT EXISTS public.usuario
(
    id_usuario serial,
    nome_usuario character varying(50) COLLATE pg_catalog."default",
    email character varying(50) COLLATE pg_catalog."default",
    senha character varying(64) COLLATE pg_catalog."default",
    editor boolean
);

ALTER TABLE IF EXISTS public.colaborador_evento
    ADD CONSTRAINT colaborador_evento_id_evento_fkey FOREIGN KEY (id_evento)
    REFERENCES public.evento (id_evento) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.colaborador_evento
    ADD CONSTRAINT colaborador_evento_id_instituicao_fkey FOREIGN KEY (id_instituicao)
    REFERENCES public.instituicao (id_instituicao) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.colaborador_grupo_eventos
    ADD CONSTRAINT fk_grupo_eventos FOREIGN KEY (id_grupo_eventos)
    REFERENCES public.grupo_eventos (id_grupo_eventos) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.colaborador_grupo_eventos
    ADD CONSTRAINT fk_instituicao FOREIGN KEY (id_instituicao)
    REFERENCES public.instituicao (id_instituicao) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.evento
    ADD CONSTRAINT fk_grupo_eventos FOREIGN KEY (id_grupo_eventos)
    REFERENCES public.grupo_eventos (id_grupo_eventos) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.evento
    ADD CONSTRAINT fk_service_file FOREIGN KEY (id_service_file)
    REFERENCES public.service_file (id_service_file) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.grupo_eventos
    ADD CONSTRAINT fk_service_file FOREIGN KEY (id_service_file)
    REFERENCES public.service_file (id_service_file) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.instituicao
    ADD CONSTRAINT fk_service_file FOREIGN KEY (id_service_file)
    REFERENCES public.service_file (id_service_file) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.localizacao_evento
    ADD CONSTRAINT localizacao_evento_id_evento_fkey FOREIGN KEY (id_evento)
    REFERENCES public.evento (id_evento) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.localizacao_evento
    ADD CONSTRAINT localizacao_evento_id_localizacao_fkey FOREIGN KEY (id_localizacao)
    REFERENCES public.localizacao (id_localizacao) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.meta_evento
    ADD CONSTRAINT meta_evento_id_evento_fkey FOREIGN KEY (id_evento)
    REFERENCES public.evento (id_evento) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.meta_evento
    ADD CONSTRAINT meta_evento_id_meta_fkey FOREIGN KEY (id_meta)
    REFERENCES public.meta (id_meta) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.meta_grupo_eventos
    ADD CONSTRAINT fk_grupo_eventos FOREIGN KEY (id_grupo_eventos)
    REFERENCES public.grupo_eventos (id_grupo_eventos) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.meta_grupo_eventos
    ADD CONSTRAINT fk_meta FOREIGN KEY (id_meta)
    REFERENCES public.meta (id_meta) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.organizador_evento
    ADD CONSTRAINT organizador_evento_id_evento_fkey FOREIGN KEY (id_evento)
    REFERENCES public.evento (id_evento) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.organizador_evento
    ADD CONSTRAINT organizador_evento_id_instituicao_fkey FOREIGN KEY (id_instituicao)
    REFERENCES public.instituicao (id_instituicao) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.organizador_grupo_eventos
    ADD CONSTRAINT fk_grupo_eventos FOREIGN KEY (id_grupo_eventos)
    REFERENCES public.grupo_eventos (id_grupo_eventos) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.organizador_grupo_eventos
    ADD CONSTRAINT fk_instituicao FOREIGN KEY (id_instituicao)
    REFERENCES public.instituicao (id_instituicao) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.participante
    ADD CONSTRAINT fk_service_file FOREIGN KEY (id_service_file)
    REFERENCES public.service_file (id_service_file) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.participante_evento
    ADD CONSTRAINT participante_evento_id_evento_fkey FOREIGN KEY (id_evento)
    REFERENCES public.evento (id_evento) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.participante_evento
    ADD CONSTRAINT participante_evento_id_participante_fkey FOREIGN KEY (id_participante)
    REFERENCES public.participante (id_participante) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.service_file_evento
    ADD CONSTRAINT fk_evento FOREIGN KEY (id_evento)
    REFERENCES public.evento (id_evento) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.service_file_evento
    ADD CONSTRAINT fk_service_file FOREIGN KEY (id_service_file)
    REFERENCES public.service_file (id_service_file) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;
END;
