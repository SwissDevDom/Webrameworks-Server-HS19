package ch.fhnw.webfr.flashcard.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.util.logging.Logger;

public class BasicFilter implements Filter {
	
	public static final String DEFAULT_BEFORE_MESSAGE_PREFIX = "Before request [";
	public static final String DEFAULT_BEFORE_MESSAGE_SUFFIX = "]";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
		logger.info("init() called");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(DEFAULT_BEFORE_MESSAGE_PREFIX);
		buffer.append("uri=").append(httpRequest.getRequestURI());
		buffer.append(DEFAULT_BEFORE_MESSAGE_SUFFIX);
		
		logger.info(buffer.toString());
		
		chain.doFilter(request, response);
	}

	public void destroy() {
		
		logger.info("destroy() called");
	}
}
