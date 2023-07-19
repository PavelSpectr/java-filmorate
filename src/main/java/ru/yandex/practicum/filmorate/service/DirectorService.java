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

    public Director getDirectorById(int id) {
        log.debug("+ getDirectorById: id={}", id);
        Director director = directorStorage.getDirectorById(id);
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

    public void deleteDirector(int id) {
        log.debug("+ deleteDirector: id={}", id);
        directorStorage.deleteDirector(id);
        log.debug("- deleteDirector");
    }
}
