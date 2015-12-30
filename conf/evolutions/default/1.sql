# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table api (
  id                            bigint not null,
  descripcion                   varchar(255),
  tematica                      varchar(255),
  url                           varchar(255),
  gratis                        boolean,
  precio_mes                    integer,
  puntuacion_media              decimal(38),
  xml                           boolean,
  json                          boolean,
  html                          boolean,
  text                          boolean,
  bytes                         boolean,
  version                       bigint not null,
  fecha_creacion                timestamp not null,
  fecha_modicacion              timestamp not null,
  constraint pk_api primary key (id)
);
create sequence api_seq;

create table opinion (
  id                            bigint not null,
  api_id                        bigint,
  usuario_id                    bigint,
  puntuacion                    integer,
  facil_manejo                  boolean,
  rapidez_respuesta             boolean,
  ventajas                      varchar(255),
  inconvenientes                varchar(255),
  version                       bigint not null,
  fecha_creacion                timestamp not null,
  fecha_modicacion              timestamp not null,
  constraint pk_opinion primary key (id)
);
create sequence opinion_seq;

create table usuario (
  id                            bigint not null,
  nick                          varchar(255),
  password                      varchar(255),
  version                       bigint not null,
  fecha_creacion                timestamp not null,
  fecha_modicacion              timestamp not null,
  constraint pk_usuario primary key (id)
);
create sequence usuario_seq;

alter table opinion add constraint fk_opinion_api_id foreign key (api_id) references api (id) on delete restrict on update restrict;
create index ix_opinion_api_id on opinion (api_id);

alter table opinion add constraint fk_opinion_usuario_id foreign key (usuario_id) references usuario (id) on delete restrict on update restrict;
create index ix_opinion_usuario_id on opinion (usuario_id);


# --- !Downs

alter table opinion drop constraint if exists fk_opinion_api_id;
drop index if exists ix_opinion_api_id;

alter table opinion drop constraint if exists fk_opinion_usuario_id;
drop index if exists ix_opinion_usuario_id;

drop table if exists api;
drop sequence if exists api_seq;

drop table if exists opinion;
drop sequence if exists opinion_seq;

drop table if exists usuario;
drop sequence if exists usuario_seq;

