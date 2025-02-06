CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists users
(
    id       uuid                                                            not null
        primary key,
    email    varchar(355)                                                    not null,
    password varchar(255),
    name     varchar(50)                                                     not null,
    provider varchar(15),
    deleted  timestamp                                                       null,
    created  timestamp default CURRENT_TIMESTAMP                             not null,
    modified timestamp default CURRENT_TIMESTAMP                             not null
);

create index users_email_deleted_created_index
    on users (email asc, deleted asc, created desc);

create table if not exists owners
(
    id       uuid                                                            not null
    primary key,
    email    varchar(355)                                                    not null,
    password varchar(255),
    name     varchar(50)                                                     not null,
    provider varchar(15),
    deleted  timestamp                                                       null,
    created  timestamp default CURRENT_TIMESTAMP                             not null,
    modified timestamp default CURRENT_TIMESTAMP                             not null
);

create index owners_email_deleted_created_index
    on owners (email asc, deleted asc, created desc);
