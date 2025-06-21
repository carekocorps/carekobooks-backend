update users
set keycloak_id = '704143b8-95d8-4f80-8e28-2c98a5d433ed'::uuid,
    updated_at = NOW()
where username = 'admin';
