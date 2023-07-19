package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Director {
    @EqualsAndHashCode.Include
    private int id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    public Director(String name) {
        this.name = name;
    }
}
