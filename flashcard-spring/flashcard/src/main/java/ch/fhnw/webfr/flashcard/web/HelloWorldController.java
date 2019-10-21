package ch.fhnw.webfr.flashcard.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.fhnw.webfr.flashcard.domain.Questionnaire;
import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;

@Controller
@RequestMapping("/hello")
public class HelloWorldController {
    
    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String sayHello(@RequestParam("name") String name) {
        List<Questionnaire> questionnaires = questionnaireRepository.findAll();
        
        String response = "Hello " + name + "<br/>" + "You have " + questionnaires.size()
                          + " Questionnaires in your repo.";
        
        return response;
    }
}