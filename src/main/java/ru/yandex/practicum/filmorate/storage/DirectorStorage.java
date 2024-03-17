package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getDirectors();

    Director getDirectorById(long id);

    long addDirector(Director director);

    long updateDirector(Director director);

    void deleteDirector(long id);
}
