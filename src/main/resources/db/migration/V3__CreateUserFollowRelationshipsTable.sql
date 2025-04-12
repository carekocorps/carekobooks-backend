CREATE TABLE `user_follow_relationships` (
    `user_following_id` BIGINT NOT NULL,
    `user_followed_id`  BIGINT NOT NULL,
    KEY `idx_ufr_followed` (`user_followed_id`),
    KEY `idx_ufr_following` (`user_following_id`),
    CONSTRAINT `fk_ufr_followed` FOREIGN KEY (`user_followed_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_ufr_following` FOREIGN KEY (`user_following_id`) REFERENCES `users` (`id`)
);
