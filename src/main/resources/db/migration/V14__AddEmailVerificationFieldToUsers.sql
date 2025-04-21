ALTER TABLE `users`
RENAME COLUMN `reset_token` TO `password_verification_token`,
RENAME COLUMN `reset_token_expires_at` TO `password_verification_token_expires_at`,
ADD COLUMN `email_verification_token` BINARY(16) DEFAULT NULL,
ADD COLUMN `email_verification_token_expires_at` DATETIME(6) DEFAULT NULL;