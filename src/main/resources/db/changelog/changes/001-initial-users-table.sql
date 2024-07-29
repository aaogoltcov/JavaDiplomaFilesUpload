create table USERS
(
    username       varchar(50)  not null unique,
    password       varchar(255)  not null,
    email          varchar(100) not null unique,
    role           varchar(50)  not null,
    id             serial primary key
);
