package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { AfterFirstFilmDateValidation.class })
public @interface AfterFirstFilmDate {
    String value() default "1895-12-27";
    String message() default "ERROR: Дата релиза — не может быть раньше 28 декабря 1895 года.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
