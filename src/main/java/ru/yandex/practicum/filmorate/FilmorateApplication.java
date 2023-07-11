package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {
		//Оставлю бины, как напоминание для себя)
		/*ApplicationContext context = */
		SpringApplication.run(FilmorateApplication.class, args);
		/*FilmController filmController = context.getBean(FilmController.class);
		UserController userController = context.getBean(UserController.class);
		ErrorHandler errorHandler = context.getBean(ErrorHandler.class);*/
	}

}
