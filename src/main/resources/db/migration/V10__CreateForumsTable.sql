CREATE TABLE `forums` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `description` VARCHAR(1000) NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `book_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_forum_book_id` (`book_id`),
    KEY `idx_forum_user_id` (`user_id`),
    CONSTRAINT `fk_forum_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
    CONSTRAINT `fk_forum_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);