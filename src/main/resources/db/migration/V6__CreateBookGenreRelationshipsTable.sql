create table book_genre_relationships
(
    book_id  bigint not null
        constraint fkswm9ahi51xlqnvb3uwr0yhcn4
            references books,
    genre_id bigint not null
        constraint fkdatsgp0y1ivl4w918j0adrkmb
            references book_genres
);
