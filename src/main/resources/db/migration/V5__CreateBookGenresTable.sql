CREATE TABLE `book_genres` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `friendly_name` VARCHAR(100) NOT NULL,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unq_book_genre_name` (`name`)
);