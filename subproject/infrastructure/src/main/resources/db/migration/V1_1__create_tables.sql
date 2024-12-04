CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists users
(
    id       uuid                                                            not null
        primary key,
    email    varchar(355)                                                    not null,
    password varchar(255)                                                    not null,
    name     varchar(50)                                                     not null,
    deleted  timestamp                                                       null,
    created  timestamp default CURRENT_TIMESTAMP                             not null,
    modified timestamp default CURRENT_TIMESTAMP                             not null
);

create table if not exists owners
(
    id       uuid                                                            not null
    primary key,
    email    varchar(355)                                                    not null,
    password varchar(255)                                                    not null,
    name     varchar(50)                                                     not null,
    deleted  timestamp                                                       null,
    created  timestamp default CURRENT_TIMESTAMP                             not null,
    modified timestamp default CURRENT_TIMESTAMP                             not null
);
