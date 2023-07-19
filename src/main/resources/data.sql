MERGE INTO mpa_ratings
    USING (VALUES (1, 'G'),
                  (2, 'PG'),
                  (3, 'PG-13'),
                  (4, 'R'),
                  (5, 'NC-17')
        ) AS new_ratings (id, name)
ON mpa_ratings.id = new_ratings.id
WHEN MATCHED THEN
    UPDATE
    SET name = new_ratings.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (new_ratings.id, new_ratings.name);

MERGE INTO genres
    USING (VALUES (1, 'Комедия'),
                  (2, 'Драма'),
                  (3, 'Мультфильм'),
                  (4, 'Триллер'),
                  (5, 'Документальный'),
                  (6, 'Боевик')
        ) AS new_genres (id, name)
ON genres.id = new_genres.id
WHEN MATCHED THEN
    UPDATE
    SET name = new_genres.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (new_genres.id, new_genres.name);

//INSERT INTO films ("id", "name", "description", "release_date", "duration_minutes", "mpa_rating_id") VALUES (1, 'Как работает Java', 'Документальный фильм, который погружает зрителя в удивительный мир программирования на языке Java.', '2022-08-02', 120, 1);
//INSERT INTO films ("id", "name", "description", "release_date", "duration_minutes", "mpa_rating_id") VALUES (2, 'Интересное программирование', '"Интересное программирование" от Яндекс Практикум: Молодой программист погружается в мир таинственных багов и зловещих алгоритмов, где каждая строка кода может привести к смертельным последствиям.', '2023-07-08', 260, 5);

//INSERT INTO film_genres ("film_id", "genre_id") VALUES (1, 5);
//INSERT INTO film_genres ("film_id", "genre_id") VALUES (2, 4);
//INSERT INTO film_genres ("film_id", "genre_id") VALUES (2, 1);

//INSERT INTO users ("id", "email", "login", "name", "birthday") VALUES (1, 'user@user.ru', 'user-login', 'Пользователь 1', '1983-01-01');
//INSERT INTO users ("id", "email", "login", "name", "birthday") VALUES (2, 'user2@user.ru', 'user2-login', 'Пользователь 2', '1984-03-02');

//INSERT INTO film_likes ("user_id", "film_id", "created_at") VALUES (1, 2, '2023-07-09 00:51:15.000000');
//INSERT INTO film_likes ("user_id", "film_id", "created_at") VALUES (2, 2, '2023-07-09 00:51:27.000000');