truncate table users restart identity cascade;

alter table users
    drop column email,
    drop column temp_email,
    drop column password,
    drop column temp_password,
    drop column role,
    drop column is_enabled,
    drop column otp,
    drop column otp_validation_type;

alter table users
    add column keycloak_id uuid not null
        constraint uk_keycloak_id
            unique;

create index idx_keycloak_id
    on users (keycloak_id);
