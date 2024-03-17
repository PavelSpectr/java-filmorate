create table if not exists directors
(
    id   integer generated by default as identity primary key,
    name varchar(255) not null
);

create table if not exists mpa_ratings
(
    id   integer generated by default as identity primary key,
    name varchar(10) not null
);

create table if not exists films
(
    id               integer generated by default as identity primary key,
    name             varchar(255) not null,
    description      varchar(200),
    release_date     date not null,
    duration_minutes integer not null constraint films_duration_minutes_check check (duration_minutes > 0),
    mpa_rating_id    integer not null constraint fk_films_mpa_rating references mpa_ratings
);

create table if not exists genres
(
    id   integer generated by default as identity primary key,
    name varchar(50) not null
);

create table if not exists film_genres
(
    film_id  integer not null references films on delete cascade,
    genre_id integer not null references genres,
    primary key (film_id, genre_id)
);

create table if not exists users
(
    id       integer generated by default as identity primary key,
    email    varchar(255) not null constraint email_unique unique,
    login    varchar(255) not null constraint login_unique unique,
    name     varchar(255) not null,
    birthday date
);

create table if not exists friendships
(
    id           integer generated by default as identity primary key,
    from_user_id integer not null references users on delete cascade,
    to_user_id   integer not null references users on delete cascade,
    constraint friendships_pk unique (from_user_id, to_user_id)
);

create table if not exists film_likes
(
    user_id    integer not null references users on delete cascade,
    film_id    integer not null references films on delete cascade,
    created_at timestamp default CURRENT_TIMESTAMP,
    unique (user_id, film_id)
);

create table if not exists film_directors
(
    film_id     integer not null references films on delete cascade,
    director_id integer not null references directors on delete cascade,
    primary key (film_id, director_id)
);

create table if not exists reviews
(
    review_id   integer generated by default as identity primary key,
    content     varchar,
    is_positive boolean not null,
    user_id     integer not null references users on delete cascade,
    film_id     integer not null references films on delete cascade,
    useful      integer
);

create table if not exists review_likes
(
    review_id   integer not null references reviews on delete cascade,
    user_id     integer not null references users on delete cascade,
    is_positive boolean not null,
    primary key (review_id, user_id)
);

alter table reviews
    alter COLUMN useful SET DEFAULT 0;

create table if not exists user_events
(
    id              integer generated by default as identity primary key,
    user_id         integer     not null references users on delete cascade,
    entity_id       integer     not null,
    event_type      varchar(20) not null,
    event_operation varchar(20) not null,
    created_at      timestamp default CURRENT_TIMESTAMP
);