CREATE TABLE `book_genre_relationships` (
    `book_id` BIGINT NOT NULL,
    `genre_id` BIGINT NOT NULL,
    KEY `idx_bgr_genre_id` (`genre_id`),
    KEY `idx_bgr_book_id` (`book_id`),
    CONSTRAINT `fk_bgr_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
    CONSTRAINT `fk_bgr_genre` FOREIGN KEY (`genre_id`) REFERENCES `book_genres` (`id`)
);
