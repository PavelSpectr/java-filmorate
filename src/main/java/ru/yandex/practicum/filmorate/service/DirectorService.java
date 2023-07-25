package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@Slf4j
public class DirectorService {

    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(@Qualifier("directorDbStorage") DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public List<Director> getDirectors() {
        log.debug("+ getDirectors");
        List<Director> directors = directorStorage.getDirectors();
        log.debug("- getDirectors: {}", directors);
        return directors;
    }

    public Director getDirectorById(int directorId) {
        log.debug("+ getDirectorById: directorId={}", directorId);
        Director director = directorStorage.getDirectorById(directorId);
        log.debug("- getDirectorById: {}", director);
        return director;
    }

    public Director addDirector(Director director) {
        log.debug("+ addDirector: {}", director);
        Director addedDirector = directorStorage.getDirectorById(directorStorage.addDirector(director));
        log.debug("- addDirector: {}", addedDirector);
        return addedDirector;
    }

    public Director updateDirector(Director director) {
        log.debug("+ updateDirector: {}", director);
        Director updatedDirector = directorStorage.getDirectorById(directorStorage.updateDirector(director));
        log.debug("- updateDirector: {}", updatedDirector);
        return updatedDirector;
    }

    public void deleteDirector(int directorId) {
        log.debug("+ deleteDirector: directorId={}", directorId);
        directorStorage.deleteDirector(directorId);
        log.debug("- deleteDirector");
    }
}
