CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `description` VARCHAR(1000) DEFAULT NULL,
    `email` VARCHAR(255) NOT NULL,
    `name` VARCHAR(50) DEFAULT NULL,
    `password` VARCHAR(255) NOT NULL,
    `role` TINYINT NOT NULL,
    `username` VARCHAR(50) NOT NULL,
    `image_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unq_user_email` (`email`),
    UNIQUE KEY `unq_user_username` (`username`),
    KEY `idx_user_image_id` (`image_id`),
    CONSTRAINT `fk_user_image` FOREIGN KEY (`image_id`) REFERENCES `images` (`id`) ON DELETE SET NULL,
    CONSTRAINT `chk_user_role` CHECK ((`role` BETWEEN 0 AND 1))
);
