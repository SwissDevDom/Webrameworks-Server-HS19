package ch.fhnw.webfr.flashcard;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ch.fhnw.webfr.flashcard.TestUtil.QuestionnaireBuilder;
import ch.fhnw.webfr.flashcard.domain.Questionnaire;
import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;
import ch.fhnw.webfr.flashcard.web.QuestionnaireController;

@RunWith(SpringRunner.class)
@WebMvcTest(QuestionnaireController.class)
public class QuestionnaireControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
    private QuestionnaireRepository questionnaireRepositoryMock;

	@Before
    public void setUp() {
		Mockito.reset(questionnaireRepositoryMock);
    }
	
	@Test
	public void create_NewQuestionnaire_ShouldReturnOK() throws Exception {
		
		Questionnaire questionnaire = new QuestionnaireBuilder("1")
				.description("MyDescription")
				.title("MyTitle")
				.build();
		
		Mockito.when(questionnaireRepositoryMock.save(questionnaire)).thenReturn(questionnaire);
		
		mockMvc.perform(post("/questionnaires")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(questionnaire)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$.title", is("MyTitle")))
				.andExpect(jsonPath("$.description", is("MyDescription")));
		
		Mockito.verify(questionnaireRepositoryMock, times(1)).save(questionnaire);
	}
	
	@Test
	public void create_NewQuestionnaireWithEmptyTitle_ShouldReturnNOK() throws Exception {
		Questionnaire questionnaire = new QuestionnaireBuilder(Long.toString(1))
				.description("MyDescription")
				.build();

		// Important: You must override Questionnaire.equals() to be able to execute these mock calls!
		Mockito.when(questionnaireRepositoryMock.save(questionnaire)).thenReturn(questionnaire);

		mockMvc.perform(post("/questionnaires")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(questionnaire)))
				.andExpect(status().isPreconditionFailed());		
		
	    Mockito.verify(questionnaireRepositoryMock, times(0)).save(questionnaire);
	}
	
	@Test
    public void findById_QuestionnaireNotExisting_ShouldReturnNotFound() throws Exception {	
	    mockMvc.perform(get("/questionnaires/{id}", 2L)
                .header("Accept", "application/json")
        )
        		.andExpect(status().isNotFound());
	    Mockito.verify(questionnaireRepositoryMock, times(0)).findById("1");
    }	
	
	@Test
	public void get_All_Questionnaires() throws Exception {
		
		Questionnaire q1 = new QuestionnaireBuilder("1")
				.title("MyTitle1")
				.description("MyDescription1")
				.build();
		
		Questionnaire q2 = new QuestionnaireBuilder("2")
				.title("MyTitle2")
				.description("MyDescription2")
				.build();
		
		List<Questionnaire> questionnaires = Arrays.asList(new Questionnaire[] { q1, q2 });
		
		Sort sort = Sort.by(Direction.ASC, "id");
		Mockito.when(questionnaireRepositoryMock.findAll(sort)).thenReturn(questionnaires);
		
		mockMvc.perform(get("/questionnaires")
				.header("Accept", "application/json")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(questionnaires)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is("1")))
				.andExpect(jsonPath("$[0].title", is("MyTitle1")))
				.andExpect(jsonPath("$[0].description", is("MyDescription1")))
				.andExpect(jsonPath("$[1].id", is("2")))
				.andExpect(jsonPath("$[1].title", is("MyTitle2")))
				.andExpect(jsonPath("$[1].description", is("MyDescription2")));
		
		
		Mockito.verify(questionnaireRepositoryMock, times(1)).findAll(sort);
	}

	@Test
	public void update_questionnaire_ShouldReturnOK() throws Exception {
		Questionnaire updatedQuestionnaire = new QuestionnaireBuilder(Long.toString(1))
				.description("MyDescription")
				.title("MyTitle")
				.build();

		Optional<Questionnaire> qOptional = Optional.of(updatedQuestionnaire);

		Mockito.when(questionnaireRepositoryMock.findById("1")).thenReturn(qOptional);
		Mockito.when(questionnaireRepositoryMock.save(updatedQuestionnaire)).thenReturn(updatedQuestionnaire);

		mockMvc.perform(put("/questionnaires/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updatedQuestionnaire)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$.title",is("MyTitle")))
				.andExpect(jsonPath("$.description", is("MyDescription")));

		Mockito.verify(questionnaireRepositoryMock, times(1)).findById("1");
		Mockito.verify(questionnaireRepositoryMock, times(1)).save(updatedQuestionnaire);
	}

	@Test
	public void delete_questionnaire_ShouldReturnOK() throws Exception {
		Questionnaire questionnaire = new QuestionnaireBuilder(Long.toString(1))
				.description("MyDescription")
				.title("MyTitle")
				.build();

		Optional<Questionnaire> qOptional = Optional.of(questionnaire);

		Mockito.when(questionnaireRepositoryMock.findById("1")).thenReturn(qOptional);

		mockMvc.perform(delete("/questionnaires/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		Mockito.verify(questionnaireRepositoryMock, times(1)).deleteById("1");
	}
}
