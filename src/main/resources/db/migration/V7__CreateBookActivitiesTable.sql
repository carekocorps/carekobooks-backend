CREATE TABLE `book_activities` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `pages_read` INT DEFAULT NULL,
    `status` TINYINT NOT NULL,
    `book_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_ba_book_id` (`book_id`),
    KEY `idx_ba_user_id` (`user_id`),
    CONSTRAINT `fk_ba_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
    CONSTRAINT `fk_ba_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `chk_ba_status` CHECK ((`status` BETWEEN 0 AND 2))
);