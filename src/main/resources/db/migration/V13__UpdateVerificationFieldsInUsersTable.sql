alter table users
    drop column verification_token,
    drop column verification_token_expires_at,
    drop column password_verification_token,
    drop column password_verification_token_expires_at,
    drop column email_verification_token,
    drop column email_verification_token_expires_at;

alter table users
    add column otp varchar(8),
    add column otp_expires_at timestamp(6),
    add column otp_validation_type varchar(255),
    add constraint users_otp_validation_type_check
        check ((otp_validation_type)::text = ANY (ARRAY['REGISTRATION'::character varying, 'PASSWORD'::character varying, 'EMAIL'::character varying]));

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
