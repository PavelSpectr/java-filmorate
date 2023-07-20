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
    private Long id;

    @NotBlank(message = "Имя режисера не должно быть пустым")
    private String name;
}
