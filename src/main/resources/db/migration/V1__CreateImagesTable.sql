CREATE TABLE `images` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `content_type` VARCHAR(50) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `size_in_bytes` BIGINT NOT NULL,
    `url` VARCHAR(1024) NOT NULL,
    PRIMARY KEY (`id`)
);