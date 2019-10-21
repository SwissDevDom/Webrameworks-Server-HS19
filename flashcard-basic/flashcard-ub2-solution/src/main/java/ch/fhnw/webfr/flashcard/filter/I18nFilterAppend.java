package ch.fhnw.webfr.flashcard.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
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
public class I18nFilterAppend implements Filter {
	
	private Properties translation;
	
	private final static String I18N_PARAM_NAME = "i18n";

	@Override
	public void init(FilterConfig filterConfig) 
			throws ServletException {
		
		String i18nFileName = filterConfig.getInitParameter(I18N_PARAM_NAME);
		
		translation = new Properties();
		final InputStream propsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(i18nFileName);
		
		try {
		
			translation.load(propsStream);
		}
		catch(IOException e) {
			
			// Das ist ein Konfigurationsfehler. Das Servlet ist permanent nicht verfügbar.
			throw new UnavailableException("I18n could not be loaded. Check the configuration.");
		}
			
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		final PrintInterceptor writer = new PrintInterceptor(response.getWriter(), translation);
		
		// Wir setzen einen neuen Wrapper für den Response. Der nächste Filter und der
		// Endpunkt der Kette, also das Servlet, werden diesen Wrapper benutzen.
		// Unser Wrapper überschreibt die getWriter Operation und sorgt dafür, dass
		// unsere eigene PrintWriter Klasse zum Zuge kommt.
		chain.doFilter(request, new HttpServletResponseWrapper((HttpServletResponse)response) {
			
			@Override public PrintWriter getWriter() {
				
				return writer; 
			}
		});
	}

	@Override
	public void destroy() {
	
		// Do nothing.
	}

	
	/**
	 * Diese Klasse überschreibt die Append-Operation. Das Servlet braucht über den
	 * Wrapper diese Operation und wir haben somit die Gelegenheit die Übersetzung
	 * an dieser Stelle auszuführen.
	 */
	private class PrintInterceptor extends PrintWriter {
		
		private Properties translation;
		
		/**
		 * Ctor.
		 * 
		 * @param writer  Der PrintWriter des 'Original'-Response
		 * @param translation  Das Properties mit den Übersetzungs Key, Value Paaren
		 */
		public PrintInterceptor(Writer writer, Properties translation) {
	        
			super(writer);
			
			this.translation = translation;
	    }
		
		@Override 
		public PrintWriter append(CharSequence message) {
	
			String m = (String)message;
			
			for(String key : translation.stringPropertyNames()) {
				m = m.replace(key, translation.getProperty(key));
			}
			
			return super.append(m);
		}		
	}
}