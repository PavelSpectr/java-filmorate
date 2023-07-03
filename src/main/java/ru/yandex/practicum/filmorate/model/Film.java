package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validator.AfterFirstFilmDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @AfterFirstFilmDate
    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    private final Set<Long> likes = new HashSet<>();

    //Все таки явное объявление конструктора более кофортно, в плане понимания кода)
    //Иначе приходится проверять все свойства, а делать все через сеттеры не очень удобно)
    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
