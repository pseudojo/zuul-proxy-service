package com.example.filter.eureka.error;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.example.filter.FilterOrder;
import com.example.filter.FilterType;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

// Error Filter (Example)
public class ExampleErrorFilter extends ZuulFilter {
	private final Logger logger = LoggerFactory.getLogger( getClass() );
	
	private static final String HEADER_RETRY_KEY = "isRetry";
	private static final String HEADER_RETRY_VALUE = "true";
	
	private final RetryCounter retryCounter = new RetryCounter();
	
	@Value("${zuul.ExampleErrorFilter.error.maxRetry:3}")
	private int maxRetry;
	
	public ExampleErrorFilter() {
		logger.error( "Create ExampleErrorFilter" );
	}
	
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = getContext();
		int statusCode = ctx.getResponseStatusCode();
		boolean isNotFound = HttpStatus.valueOf( statusCode ).is4xxClientError(); 
		boolean isNotUsable = HttpStatus.valueOf( statusCode ).is5xxServerError();
		
		logger.info( "Status : {} / isNotFound : {} / isNotUsable : {}", statusCode, isNotFound, isNotUsable );
		
		if (isNotFound)
			logger.warn( "URL is not found(Status : {})...", statusCode );
		else if (isNotUsable)
			logger.warn( "URL is not usable(Status : {})...", statusCode );
		
		return (isNotFound || isNotUsable);
	}
	
	@Override
	public int filterOrder() {
		return FilterOrder.ErrorOrder.ExampleErrorFilter;
	}

	@Override
	public String filterType() {
		return FilterType.ERROR;
	}
	
	private final RequestContext getContext() {
		return RequestContext.getCurrentContext();
	}

	@Override
	public Object run() {
		// don't redirect SendErrorFilter (only test)
		final HttpServletRequest reqCtx = getContext().getRequest();
		final HttpServletResponse resCtx = getContext().getResponse();
		
		String requestURL = reqCtx.getRequestURL().toString();
		String isRetry = resCtx.getHeader( HEADER_RETRY_KEY );
		if ( HEADER_RETRY_VALUE.equals( isRetry ) ) 
			retryCounter.reset( requestURL );
		
		int currentRetry = ( int ) retryCounter.incrementAndGet( requestURL );
		if (currentRetry <= maxRetry) {
			try {
				resCtx.addHeader( HEADER_RETRY_KEY, HEADER_RETRY_VALUE );
				resCtx.sendRedirect( requestURL );
				logger.info( "Redirect URL(#{}) : {}", currentRetry, requestURL );
			} catch ( IOException e ) {
				logger.error( "Occurs exception...", e );
			}
		} else { 
			logger.error( "Reach maximum retry count... {}", (currentRetry - 1) );
			logger.error( "ERROR URL : {}", requestURL );
			retryCounter.reset( requestURL );
		}
		
		return null;
	}
	
	private static class RetryCounter {
		private ConcurrentHashMap<String, AtomicInteger> internalMap = new ConcurrentHashMap<>();
		
		public int incrementAndGet(String url) {
			if (internalMap.containsKey( url )) {
				return internalMap.get( url ).incrementAndGet();
			} else {
				AtomicInteger al = new AtomicInteger(0);
				internalMap.put( url, al );
				return al.incrementAndGet();
			}				
		}
		
		public void reset(String url) {
			internalMap.remove( url );
		}
	}
}
