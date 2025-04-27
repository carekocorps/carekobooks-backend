alter table book_thread_replies
    add column parent_id bigint
        constraint fkcon2yjnx66ayvdn8y84i3w15g
            references book_thread_replies;
