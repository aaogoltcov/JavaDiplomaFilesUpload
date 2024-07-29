create table FILES
(
    file_name varchar(255)   not null unique,
    file_data bytea          not null,
    edited_at timestamp      not null default now(),
    size      decimal(10, 2) not null,
    id        serial primary key
);