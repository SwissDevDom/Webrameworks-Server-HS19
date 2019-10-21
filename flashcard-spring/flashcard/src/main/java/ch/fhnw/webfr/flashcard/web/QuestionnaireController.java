
package ch.fhnw.webfr.flashcard.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ch.fhnw.webfr.flashcard.domain.Questionnaire;
import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;

@Controller
@RequestMapping("/questionnaires")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

	@RequestMapping(method = RequestMethod.GET)
	public String findAll(Model model){
		
		List<Questionnaire> questionnaires = questionnaireRepository.findAll();
		
		model.addAttribute("questionnaires", questionnaires);
		
		return "questionnaires/list";
	}

	/**
	 * Basic Questionnaires Solution without ThymeLeaf
	 * 
	 */
	// public void findAll(HttpServletResponse response, HttpServletRequest request) throws IOException {
    //     List<Questionnaire> questionnaires = questionnaireRepository.findAll();

    //     PrintWriter writer = response.getWriter();
	// 	writer.append("<html><head><title>Example</title></head><body>");
	// 	writer.append("<h3>Fragebögen</h3>");
		
	// 	for (Questionnaire questionnaire : questionnaires) {
			
	// 		String url = request.getRequestURL() + "/" + questionnaire.getId().toString();
			
	// 		writer.append("<p><a href='" + response.encodeURL(url) + "'>" + questionnaire.getTitle() + "</a></p>");
	// 	}
		
	// 	writer.append("</body></html>");
    // }

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String findById(@PathVariable String id, Model model) {
		
		if(questionnaireRepository.existsById(id)) {
			Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
			model.addAttribute("questionnaire", questionnaire.get());
			
			return "questionnaires/show";
		}
		
		return "404";
	}
	/**
	 * Basic Question-Detail Solution without ThymeLeaf
	 * 
	 */
    // public void findById(@PathVariable String id, HttpServletResponse response, HttpServletRequest request) throws IOException {
	   
	// 	Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
		
	// 	PrintWriter writer = response.getWriter();
	// 	writer.append("<html lang='en'><head><title>Example</title></head><body>");
	// 	writer.append("<h2>Questionnaire</h2>");
		
	// 	if (questionnaire.isPresent()) {
			
	// 		writer.append("<h3>" + questionnaire.get().getTitle() + "</h3>");
	// 		writer.append("<p>" + questionnaire.get().getDescription() + "</p>");	
			
	// 	} else {
			
	// 		writer.append("<p><em>no questionnaire found</em></p>");
	// 	}
		
	// 	writer.append("<a href='" + request.getContextPath() + "/questionnaires'>Back</a>");
		
	// 	writer.append("</body></html>");
	// }
	
	@RequestMapping(method = RequestMethod.GET, params = { "create-form" })
	public String getForm(Model model) {
		
		model.addAttribute("questionnaire", new Questionnaire());
		
		return "questionnaires/create";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid Questionnaire questionnaire, BindingResult result) {

		if (result.hasErrors()) {
            return "questionnaires/create";
		}
		
		questionnaireRepository.save(questionnaire);

		return "redirect:questionnaires";
	 }

	@RequestMapping(value = "/{id}",method = RequestMethod.GET, params = { "update-form" })
	public String getUpdateForm(@PathVariable String id, Model model) {
		
		if(questionnaireRepository.existsById(id)) {
			
			Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
			model.addAttribute("questionnaire", questionnaire.get());
			
			return "questionnaires/update";
		}
		
		return "404";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@PathVariable String id, @Valid Questionnaire questionnaire, BindingResult result) {

		if (result.hasErrors()) {
            return "questionnaires/update";
		}
		
		if (questionnaireRepository.existsById(id)) {

			Optional<Questionnaire> q = questionnaireRepository.findById(id);
			
			q.get().setTitle(questionnaire.getTitle());
			q.get().setDescription(questionnaire.getDescription());
			
			questionnaireRepository.save(q.get());
			
			return "redirect:/questionnaires";
		
		}

		return "404";
	 }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable String id) {

		if (questionnaireRepository.existsById(id)) {
			questionnaireRepository.deleteById(id);

			return "redirect:/questionnaires";
		}

		return "404";
	 }
}