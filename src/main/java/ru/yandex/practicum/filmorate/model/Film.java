package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.FilmMinReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Название не должно быть пустым")
    private String name;

    @Size(max = 200, message = "Длина описания не должна превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не должна быть пустой")
    @FilmMinReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    @NotNull(message = "Продолжительность фильма не должна быть пустой")
    private Integer duration;

    private Set<Genre> genres;

    // Евгения: это поле добавлено поскольку без него не проходят тесты, оставшаяся реализация в рамках другой задачи
    private Set<Director> directors = new HashSet<>();
    private MpaRating mpa;
    // Евгения: мы убрали список лайков за ненадобностью, оставляем вместо него количество лайков,
    // по ним будет проходить сортировка
    private int likesCount;
}