-- public.board definition

-- Drop table

-- DROP TABLE public.board;

CREATE TABLE public.board
(
    board_id      serial4                             NOT NULL,
    title         varchar(255)                        NOT NULL,
    "content"     varchar(255)                        NOT NULL,
    writer_id     int4                                NOT NULL,
    create_date   timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified_date timestamp                           NULL
);
CREATE UNIQUE INDEX board_pkey ON public.board USING btree (board_id);

-- public."comment" definition

-- Drop table

-- DROP TABLE public."comment";

CREATE TABLE public."comment"
(
    comment_id  serial4                             NOT NULL,
    board_id    int4                                NOT NULL,
    "content"   varchar(255)                        NOT NULL,
    writer_id   int4                                NOT NULL,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT comment_pk PRIMARY KEY (comment_id)
);

-- public.dummy_data definition

-- Drop table

-- DROP TABLE public.dummy_data;

CREATE TABLE public.dummy_data
(
    dummy_id     serial4                             NOT NULL,
    string_value varchar(255)                        NOT NULL,
    number_value int4                                NOT NULL,
    create_date  timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT dummy_data_pkey PRIMARY KEY (dummy_id)
);

-- public.item definition

-- Drop table

-- DROP TABLE public.item;

CREATE TABLE public.item
(
    item_id     int4 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
    item_name   varchar                                                                                                         NOT NULL,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP                                                                             NOT NULL,
    CONSTRAINT item_pk PRIMARY KEY (item_id)
);

-- public."member" definition

-- Drop table

-- DROP TABLE public."member";

CREATE TABLE public."member"
(
    member_id   serial4                             NOT NULL,
    "name"      varchar(255)                        NOT NULL,
    email       varchar(255)                        NOT NULL,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT member_pkey PRIMARY KEY (member_id)
);
CREATE UNIQUE INDEX "IDX_MEMBER_EMAIL" ON public.member USING btree (email) WITH (deduplicate_items='true');

-- public.member_password definition

-- Drop table

-- DROP TABLE public.member_password;

CREATE TABLE public.member_password
(
    member_password_id serial4                             NOT NULL,
    member_id          int4                                NOT NULL,
    member_password    varchar(255)                        NOT NULL,
    expire_date        timestamp                           NOT NULL,
    create_date        timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT member_password_pkey PRIMARY KEY (member_password_id)
);
CREATE INDEX member_password_member_id_idx ON public.member_password USING btree (member_id, expire_date);

-- public.member_role definition

-- Drop table

-- DROP TABLE public.member_role;

CREATE TABLE public.member_role
(
    member_role_id serial4 NOT NULL,
    member_id      int4    NOT NULL,
    role_id        int4    NOT NULL,
    CONSTRAINT member_role_pk PRIMARY KEY (member_role_id)
);

-- public."role" definition

-- Drop table

-- DROP TABLE public."role";

CREATE TABLE public."role"
(
    role_id   serial4 NOT NULL,
    role_name varchar NOT NULL,
    member_id int4    NOT NULL,
    CONSTRAINT role_pk PRIMARY KEY (role_id)
);

-- public.recruit definition

-- Drop table

-- DROP TABLE public.recruit;

CREATE TABLE public.recruit (
    recruit_id serial4 NOT NULL,
    company_id int4 NOT NULL,
    "type" varchar NOT NULL,
    title varchar NOT NULL,
    description text NULL,
    salary varchar NOT NULL,
    "location" varchar NOT NULL,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_date timestamp NULL,
    CONSTRAINT recruit_pk PRIMARY KEY (recruit_id)
);
CREATE INDEX recruit_company_id_idx ON public.recruit USING btree (company_id);

-- public.company definition

-- Drop table

-- DROP TABLE public.company;

CREATE TABLE public.company (
    company_id serial4 NOT NULL,
    company_name varchar NOT NULL,
    description text NULL,
    contact_email varchar NOT NULL,
    contact_phone varchar NULL,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_date timestamp NULL,
    CONSTRAINT company_pk PRIMARY KEY (company_id)
);

-- public.survey definition

-- Drop table

-- DROP TABLE public.survey;

CREATE TABLE public.survey (
	survey_id serial4 NOT NULL,
	title varchar NOT NULL,
	description text NULL,
	status varchar DEFAULT 'WAITING'::character varying NOT NULL,
	start_date timestamp NOT NULL,
	end_date timestamp NOT NULL,
	create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	update_date timestamp NULL,
	CONSTRAINT survey_pk PRIMARY KEY (survey_id)
);

-- public.survey_question definition

-- Drop table

-- DROP TABLE public.survey_question;

CREATE TABLE public.survey_question (
	survey_question_id serial4 NOT NULL,
	survey_id int4 NOT NULL,
	title varchar NOT NULL,
	description text NULL,
	"type" varchar DEFAULT 'OBJECTIVE'::character varying NOT NULL,
	required bool DEFAULT false NOT NULL,
	max_selectable_objectives int4 DEFAULT 1 NOT NULL,
	sort_order int4 DEFAULT 1 NOT NULL,
	create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	update_date timestamp NULL,
	CONSTRAINT survey_question_pk PRIMARY KEY (survey_question_id),
	CONSTRAINT survey_question_fk FOREIGN KEY (survey_id) REFERENCES public.survey(survey_id)
);

-- public.survey_question_objective definition

-- Drop table

-- DROP TABLE public.survey_question_objective;

CREATE TABLE public.survey_question_objective (
	objective_id serial4 NOT NULL,
	survey_id int4 NOT NULL,
	survey_question_id int4 NOT NULL,
	objective_value varchar NOT NULL,
	objective_text varchar NOT NULL,
	sort_order int4 DEFAULT 1 NOT NULL,
	create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	update_date timestamp NULL,
	CONSTRAINT survey_question_objective_pk PRIMARY KEY (objective_id),
	CONSTRAINT survey_question_objective_fk FOREIGN KEY (survey_question_id) REFERENCES public.survey_question(survey_question_id)
);
