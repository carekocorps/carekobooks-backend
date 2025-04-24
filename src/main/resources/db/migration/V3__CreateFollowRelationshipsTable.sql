create table user_follow_relationships
(
    user_following_id bigint not null
        constraint fk6v5kfjwecu939s7l1mw83bbc1
            references users,
    user_followed_id  bigint not null
        constraint fk9vspqgphmejqyaxjx5ftjunwe
            references users
);
