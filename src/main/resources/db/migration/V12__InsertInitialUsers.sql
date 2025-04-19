INSERT INTO `users` (`created_at`, `updated_at`, `description`, `email`, `name`, `password`, `role`, `username`, `image_id`)
SELECT '2025-04-19 13:29:19.979583', '2025-04-19 13:29:19.979583', 'admin', 'admin@gmail.com', 'admin', '$2a$10$TuGgPj88K2O7bZGrk0yuMuFZcwq6BZGK5eszL9t2MNQsxpGe9/oJu', 0, 'admin', NULL
WHERE NOT EXISTS (SELECT 1 FROM carekobooks.users WHERE email = 'admin@gmail.com' LIMIT 1);

INSERT INTO `users` (`created_at`, `updated_at`, `description`, `email`, `name`, `password`, `role`, `username`, `image_id`)
SELECT '2025-04-19 13:29:39.779430', '2025-04-19 13:29:39.779430', 'string', 'string@gmail.com', 'string', '$2a$10$YJbbR3B0ekhiaHz7SzdYxelyCk3btsPbtKIWkyisxZ981AdyGCFSa', 0, 'string', NULL
WHERE NOT EXISTS (SELECT 1 FROM carekobooks.users WHERE email = 'string@gmail.com' LIMIT 1);
