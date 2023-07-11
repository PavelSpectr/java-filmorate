package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User {
    private Long id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    //Все таки явное объявление конструктора более кофортно, в плане понимания кода)
    //Иначе приходится проверять все свойства, а делать все через сеттеры не очень удобно)
    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
