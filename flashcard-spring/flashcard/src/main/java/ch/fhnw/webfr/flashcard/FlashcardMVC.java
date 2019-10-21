package ch.fhnw.webfr.flashcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
public class FlashcardMVC {

	public static void main(String[] args) {
		SpringApplication.run(FlashcardMVC.class, args);
	}

	//Validation auf Datenbank
	// @Bean
	// public ValidatingMongoEventListener validatingMongoEventListener() {
	// 	return new ValidatingMongoEventListener(validator());
	// }

	// @Bean
	// public LocalValidatorFactoryBean validator() {
	// 	return new LocalValidatorFactoryBean();
	// }

}
