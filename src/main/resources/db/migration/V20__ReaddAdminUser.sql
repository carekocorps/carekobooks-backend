INSERT INTO users (created_at, updated_at, description, display_name, username, image_id, otp_expires_at, keycloak_id)
values (NOW(), NOW(), NULL, 'admin', 'admin', NULL, NULL, '3af6baea-66d2-46b1-aa7b-4283f3683ff4'::uuid)
on conflict (username) do nothing;
