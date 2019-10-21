package ch.fhnw.webfr.flashcard.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * UB2 Lösung: Übersetzungsfilter für die Flashcard Anwendung.
 * 
 * @author stefanmeichtry
 */
public class I18nFilterStream implements Filter {
	
	private Properties dictionary;
	
	private final static String I18N_PARAM_NAME = "i18n";

	@Override
	public void init(FilterConfig filterConfig) 
			throws ServletException {
		
		String resource = filterConfig.getInitParameter(I18N_PARAM_NAME);
        
        if(resource == null) {
                resource = "messages.properties";       
        }
        
        try {
        	
        	dictionary = new Properties();
        	dictionary.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource));
        	
        } catch (IOException e) {
            
            throw new UnavailableException("Filter kann nicht initialisiert werden. Resource nicht gefunden.");
        }
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		// Wir erzeugen unseren eigenen Stream.
		ResponseContent myResponse = new ResponseContent((HttpServletResponse)response);
        
		// Setzen den Stream als ResponseWrapper für alle weiteren Filter und das
		// Servlet.
        chain.doFilter(request, myResponse);
        
        // Das Servlet hat in unseren Stream geschrieben. Jetzt holen wir uns
        // den Inhalt (Text) und übersetzen den Inhalt.
        // Dann schreiben wir den übersetzten Text auf den Original-Stream.
        response.getWriter().append(translate(dictionary, myResponse.getContent()));
	}

	@Override
	public void destroy() {
	
		// Do nothing.
	}

	private String translate(Properties dictionary, String content) {
        
        for(String key : dictionary.stringPropertyNames()) {
        	content = content.replace(key, dictionary.getProperty(key));
		}
        
        return content;
	}
	
	private class ResponseContent extends HttpServletResponseWrapper {
        
        private StringWriter content;

        public ResponseContent(HttpServletResponse response) {
                super(response);
                
                content = new StringWriter();
        }

        @Override
        public PrintWriter getWriter() throws IOException {
                
                return new PrintWriter(content);
        }
        
        public String getContent() {
                return content.toString();
        }
	}
}