CREATE TABLE `book_reviews` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `content` VARCHAR(5000) NOT NULL,
    `score` INT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `book_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_br_book_id` (`book_id`),
    KEY `idx_br_user_id` (`user_id`),
    CONSTRAINT `fk_br_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
    CONSTRAINT `fk_br_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);