create table users
(
    id                                     bigint generated by default as identity
        primary key,
    created_at                             timestamp(6),
    updated_at                             timestamp(6),
    description                            varchar(1000),
    display_name                           varchar(50),
    email                                  varchar(255) not null
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    email_verification_token               varchar(8),
    email_verification_token_expires_at    timestamp(6),
    is_enabled                             boolean      not null,
    password                               varchar(255) not null,
    password_verification_token            varchar(8),
    password_verification_token_expires_at timestamp(6),
    role                                   varchar(255) not null
        constraint users_role_check
            check ((role)::text = ANY ((ARRAY ['USER'::character varying, 'ADMIN'::character varying])::text[])),
    username                               varchar(50)  not null
        constraint ukr43af9ap4edm43mmtq01oddj6
            unique,
    verification_token                     varchar(8),
    verification_token_expires_at          timestamp(6),
    image_id                               bigint
        constraint fk17herqt2to4hyl5q5r5ogbxk9
            references images
            on delete set null
);

create index idx_username
    on users (username);

create index idx_email
    on users (email);
