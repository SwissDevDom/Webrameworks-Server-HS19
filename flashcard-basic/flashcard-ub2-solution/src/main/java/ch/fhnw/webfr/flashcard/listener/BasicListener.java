package ch.fhnw.webfr.flashcard.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.util.logging.Logger;

import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;
import ch.fhnw.webfr.flashcard.util.QuestionnaireInitializer;

public class BasicListener implements ServletContextListener {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private final static String MODE = "mode";
	private final static String TEST_MODE = "test";
	private final static String REPO_KEY_NAME = "questionnaireRepository";

	public void contextInitialized(ServletContextEvent sce) {
		
		String mode = (String) sce.getServletContext().getInitParameter(MODE);
		
		QuestionnaireRepository repo = null;
		
		if ((mode != null) && (mode.equals(TEST_MODE))) {
			
			repo = new QuestionnaireInitializer().initRepoWithTestData();
			
		} else {
			
			repo = QuestionnaireRepository.getInstance();
		}
		
		sce.getServletContext().setAttribute(REPO_KEY_NAME, repo);
		
		logger.info("mode is " + mode);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		
		// not used here
	}
}
