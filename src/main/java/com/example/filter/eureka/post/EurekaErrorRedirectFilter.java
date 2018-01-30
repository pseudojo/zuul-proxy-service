package com.example.filter.eureka.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.example.filter.FilterOrder;
import com.example.filter.FilterType;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class EurekaErrorRedirectFilter extends ZuulFilter {
	
	private final Logger logger = LoggerFactory.getLogger( getClass() );
	
	@Override
	public boolean shouldFilter() {
		int statusCode = RequestContext.getCurrentContext().getResponseStatusCode();
		boolean is4XXError = HttpStatus.valueOf( statusCode ).is4xxClientError();
		boolean is5XXError = HttpStatus.valueOf( statusCode ).is5xxServerError();
		
		return (is4XXError || is5XXError);
	}

	@Override
	public Object run() {
		int statusCode = RequestContext.getCurrentContext().getResponseStatusCode();
		boolean is4XXError = HttpStatus.valueOf( statusCode ).is4xxClientError();
		boolean is5XXError = HttpStatus.valueOf( statusCode ).is5xxServerError();
		
		if (is4XXError || is5XXError) {
			logger.warn( "Redirect error filter..." );
			throw new RuntimeException( 
				"Occur error : " + RequestContext.getCurrentContext().getResponseStatusCode() );
		}
		
		return null;
	}

	@Override
	public String filterType() {
		return FilterType.POST;
	}

	@Override
	public int filterOrder() {
		return FilterOrder.PostRouteOrder.EurekaErrorRedirectFilter;
	}
	
}
