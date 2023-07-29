package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> getDirectors() {
        log.info("+ getDirectors");
        List<Director> directors = directorService.getDirectors();
        log.info("- getDirectors: {}", directors);
        return directors;
    }

    @GetMapping("/{directorId}")
    public Director getDirectorById(@PathVariable long directorId) {
        log.info("+ getDirectorById: directorId={}", directorId);
        Director director = directorService.getDirectorById(directorId);
        log.info("- getDirectorById: {}", director);
        return director;
    }

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        log.info("+ addDirector: {}", director);
        Director addedDirector = directorService.addDirector(director);
        log.info("- addDirector: {}", addedDirector);
        return addedDirector;
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        log.info("+ updateDirector: {}", director);
        Director updatedDirector = directorService.updateDirector(director);
        log.info("- updateDirector: {}", updatedDirector);
        return updatedDirector;
    }

    @DeleteMapping("/{directorId}")
    public void deleteDirector(@PathVariable long directorId) {
        log.info("+ deleteDirector: directorId={}", directorId);
        directorService.deleteDirector(directorId);
        log.info("- deleteDirector");
    }
}
