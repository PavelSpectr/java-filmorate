package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenreById(Long genreId) {
        log.debug("+ getGenreById: genreId={}", genreId);
        Genre genre = genreStorage.getGenreById(genreId);
        log.debug("- getGenreById: {}", genre);
        return genre;
    }

    public List<Genre> getGenres() {
        log.debug("+ getGenres");
        List<Genre> genres = genreStorage.getGenres();
        log.debug("- getGenres: {}", genres);
        return genres;
    }
}