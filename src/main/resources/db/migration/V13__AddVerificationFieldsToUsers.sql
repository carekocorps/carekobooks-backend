ALTER TABLE `users`
ADD COLUMN `is_enabled` BIT(1) DEFAULT NULL,
ADD COLUMN `verification_token` BINARY(16) DEFAULT NULL,
ADD COLUMN `verification_token_expires_at` DATETIME(6) DEFAULT NULL,
ADD COLUMN `reset_token` BINARY(16) DEFAULT NULL,
ADD COLUMN `reset_token_expires_at` DATETIME(6) DEFAULT NULL;

UPDATE `users`
SET `is_enabled` = b'1'
WHERE `created_at` < NOW();

ALTER TABLE `users`
MODIFY COLUMN `is_enabled` BIT(1) NOT NULL;