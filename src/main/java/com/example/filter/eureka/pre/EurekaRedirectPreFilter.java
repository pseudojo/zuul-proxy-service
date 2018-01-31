package com.example.filter.eureka.pre;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.filter.FilterOrder;
import com.example.filter.FilterType;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

// Eureka Pre-route Filter (Redirect)
public class EurekaRedirectPreFilter extends ZuulFilter {
	private Logger logger = LoggerFactory.getLogger( getClass() );
	
	private String type = FilterType.PRE;
	
	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	@Override
	public int filterOrder() {
		return FilterOrder.PreRouteOrder.EurekaRedirectPreFilter;
	}

	@Override
	public String filterType() {
		return type;
	}
	
	public void disableFilter() {
		type = FilterType.DISABLED;
	}
	
	public void enableFilter() {
		type = FilterType.PRE;
	}

	private static final String[] redirectURLs = 
		{ "/eureka/css/", "/eureka/fonts/", "/eureka/images/", "/eureka/js/" };
	
	@Override
	public Object run() {
		RequestContext.getCurrentContext().unset();
		try {
			RequestContext.getCurrentContext().getResponse().getOutputStream().println( "Test" );
			RequestContext.getCurrentContext().getResponse().getOutputStream().flush();
		} catch (Throwable ignore) { 
			
		}
		
		return null;
	}
	
	/*
	@Override
	public Object run() {
		RequestContext rctx = RequestContext.getCurrentContext();
		HttpServletRequest request = rctx.getRequest();
		
		final String requestURL = request.getRequestURL().toString();
		logger.info("Request Method : {}, Request Original URL : {}", request.getMethod(), request.getRequestURL().toString());
		
		boolean isRedirect = false;
		String redirectURL = null;
		for (String urlFilter : redirectURLs) {
			if ( requestURL.contains( urlFilter ) && !requestURL.contains( "/eureka" + urlFilter )) {
				isRedirect = true;
				redirectURL = urlFilter;
			}
		}
		
		if (isRedirect) {
			try {
				String url = requestURL.replaceFirst( redirectURL, ("/eureka" + redirectURL) );
				rctx.getResponse().sendRedirect( url );
				logger.info( "Send to redirect URL : {}", url );
			} catch(MalformedURLException mue) {
	            logger.error("Cannot forward to outage period website");
	        } catch ( IOException e ) {
				logger.error( "Unknown error", e );
			} 
		}
		return null;
	}
	*/
}
