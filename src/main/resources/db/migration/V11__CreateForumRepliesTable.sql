CREATE TABLE `forum_replies` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `content` VARCHAR(1000) NOT NULL,
    `forum_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_fr_forum_id` (`forum_id`),
    KEY `idx_fr_user_id` (`user_id`),
    CONSTRAINT `fk_fr_forum` FOREIGN KEY (`forum_id`) REFERENCES `forums` (`id`),
    CONSTRAINT `fk_fr_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);