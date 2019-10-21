package ch.fhnw.webfr.flashcard.persistence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.fhnw.webfr.flashcard.domain.Questionnaire;

public interface QuestionnaireRepository extends MongoRepository<Questionnaire, String> {
	
	/**
	 * Von uns hinzugefügte Query-Methode. Existiert nicht im Mongo-DB interface.
	 * 
	 * @param title Der Questionnaire Titel
	 * @return  Eine Liste mit Questionnaires, die exakt diesen Titel haben
	 */
	List<Questionnaire> findByTitle(String title);
}
