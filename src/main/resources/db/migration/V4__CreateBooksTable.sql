CREATE TABLE `books` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `author` VARCHAR(255) NOT NULL,
    `published_at` DATE DEFAULT NULL,
    `publisher` VARCHAR(255) NOT NULL,
    `review_average_score` DOUBLE DEFAULT NULL,
    `synopsis` VARCHAR(1000) DEFAULT NULL,
    `title` VARCHAR(255) NOT NULL,
    `total_pages` INT NOT NULL,
    `user_average_score` DOUBLE DEFAULT NULL,
    `image_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_book_image_id` (`image_id`),
    CONSTRAINT `fk_book_image` FOREIGN KEY (`image_id`) REFERENCES `images` (`id`) ON DELETE SET NULL
);