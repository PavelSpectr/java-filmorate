package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getDirectors();

    Director getDirectorById(int id);

    int addDirector(Director director);

    int updateDirector(Director director);

    void deleteDirector(int id);
}
