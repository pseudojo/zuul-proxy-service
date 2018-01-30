package com.example.filter.eureka.post;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.example.filter.FilterOrder;
import com.example.filter.FilterType;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

// Eureka Post-route Filter (Redirect)
public class EurekaRedirectPostFilter extends ZuulFilter {
	private Logger logger = LoggerFactory.getLogger( getClass() );
	
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		int statusCode = ctx.getResponseStatusCode();
		
		boolean shouldFilter = HttpStatus.valueOf(statusCode).is4xxClientError();
		if (shouldFilter)
			logger.warn( "Eureka resource is not found..." );
		
		return shouldFilter;
	}
	
	@Override
	public int filterOrder() {
		return FilterOrder.PostRouteOrder.EurekaRedirectPostFilter;
	}

	@Override
	public String filterType() {
		return FilterType.POST;
	}

	private static final String[] redirectURLs = 
		{ "/eureka/css/", "/eureka/fonts/", "/eureka/images/", "/eureka/js/" };
	
	private boolean shouldRedirect() {
		RequestContext rctx = RequestContext.getCurrentContext();
		HttpServletRequest request = rctx.getRequest();
		
		final String requestURL = request.getRequestURL().toString();
		logger.info("Request Method : {}, Request Original URL : {}", request.getMethod(), request.getRequestURL().toString());
		
		boolean isRedirect = false;
		for (String urlFilter : redirectURLs) {
			if ( requestURL.contains( urlFilter ) && !requestURL.contains( "/eureka" + urlFilter )) {
				isRedirect = true;
				break;
			}
		}
		
		return isRedirect;
	}
	
	private String getRedirectURL(String originalURL) {
		String urlFilter = null;
		for (String urlPiece : redirectURLs) {
			if ( originalURL.contains( urlPiece ) && !originalURL.contains( "/eureka" + urlPiece )) {
				urlFilter = urlPiece;
				break;
			}
		}
		
		if (null == urlFilter)
			return originalURL;
		else
			return originalURL.replaceFirst( urlFilter, ("/eureka" + urlFilter) );
	}
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		final String requestURL = request.getRequestURL().toString();
		logger.info("Request Method : {}, Request Original URL : {}", request.getMethod(), request.getRequestURL().toString());
		
		if (shouldRedirect()) {
			try {
				String url = getRedirectURL( requestURL );
				ctx.getResponse().sendRedirect( url );
				logger.info( "Send to redirect URL : {}", url );
			} catch(MalformedURLException mue) {
	            logger.error("Cannot forward to outage period website");
	        } catch ( IOException e ) {
				logger.error( "Unknown error", e );
			} 
		}
		
		return null;
	}
}
