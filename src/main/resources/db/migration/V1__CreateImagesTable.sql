create table images
(
    id bigint generated by default as identity
        primary key,
    created_at    timestamp(6),
    updated_at    timestamp(6),
    content_type  varchar(50)   not null,
    name          varchar(255)  not null,
    size_in_bytes bigint        not null,
    url           varchar(1024) not null
);
