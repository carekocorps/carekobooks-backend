CREATE TABLE `book_progresses` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `is_favorite` BIT(1) NOT NULL,
    `pages_read` INT DEFAULT NULL,
    `score` INT DEFAULT NULL,
    `status` TINYINT NOT NULL,
    `book_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_bp_book_id` (`book_id`),
    KEY `idx_bp_user_id` (`user_id`),
    CONSTRAINT `fk_bp_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
    CONSTRAINT `fk_bp_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `chk_bp_status` CHECK ((`status` BETWEEN 0 AND 2))
);