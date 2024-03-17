package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> getDirectors() {
        return directorStorage.getDirectors();
    }

    public Director getDirectorById(long directorId) {
        return directorStorage.getDirectorById(directorId);
    }

    public Director addDirector(Director director) {
        return directorStorage.getDirectorById(directorStorage.addDirector(director));
    }

    public Director updateDirector(Director director) {
        return directorStorage.getDirectorById(directorStorage.updateDirector(director));
    }

    public void deleteDirector(long directorId) {
        directorStorage.deleteDirector(directorId);
    }
}
