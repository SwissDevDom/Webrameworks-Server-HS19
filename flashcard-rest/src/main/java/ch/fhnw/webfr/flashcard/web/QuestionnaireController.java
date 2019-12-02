package ch.fhnw.webfr.flashcard.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ch.fhnw.webfr.flashcard.domain.Questionnaire;
import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;

@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private QuestionnaireRepository questionnaireRepository;
	
	@GetMapping
	public ResponseEntity<List<Questionnaire>> findAll() {
		
		Sort sort = Sort.by(Direction.ASC, "id");
		List<Questionnaire> questionnaires = questionnaireRepository.findAll(sort);
		
		log.debug("Found " + questionnaires.size() + " questionnaires");
		
		return new ResponseEntity<List<Questionnaire>>(questionnaires, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Questionnaire> find(@PathVariable String id) {
		
		Optional<Questionnaire> q = questionnaireRepository.findById(id);
		
		if(q.isPresent()) {
			log.debug("Found " + q.get().getId());
			return new ResponseEntity<Questionnaire>(q.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
	
	@PostMapping
	public ResponseEntity<Questionnaire> create(@Valid @RequestBody Questionnaire q, BindingResult result) {
			
		if(result.hasErrors()) {
			return new ResponseEntity<Questionnaire>(HttpStatus.PRECONDITION_FAILED);
		}
		
		Questionnaire questionnaire = questionnaireRepository.save(q);
		log.debug("Created questionnaire with id=" + questionnaire.getId());
		
		return new ResponseEntity<Questionnaire>(questionnaire, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Questionnaire> update(@PathVariable String id,
												@Valid @RequestBody Questionnaire questionnaire, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<Questionnaire>(HttpStatus.PRECONDITION_FAILED);
		}

		Optional<Questionnaire> questionnaireOptional = questionnaireRepository.findById(id);

		if (questionnaireOptional.isPresent()) {
			Questionnaire updateQuestionnaire = questionnaireOptional.get();

			updateQuestionnaire.setTitle(questionnaire.getTitle());
			updateQuestionnaire.setDescription(questionnaire.getDescription());

			questionnaire = questionnaireRepository.save(updateQuestionnaire);
			log.debug("Updated questionnaire with id=" + updateQuestionnaire.getId());
			return new ResponseEntity<Questionnaire>(updateQuestionnaire, HttpStatus.OK);
		}

		log.debug("No questionnaire with id=" + id + " found");

		return new ResponseEntity<Questionnaire>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> delete(@PathVariable String id) {

		Optional<Questionnaire> questionnaireOptional = questionnaireRepository.findById(id);

		if (questionnaireOptional.isPresent()) {
			questionnaireRepository.deleteById(id);
			log.debug("Deleted questionnaire with id=" + id);
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}

		log.debug("No questionnaire with id=" + id + " found");

		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}

}
