package com.example.filter.eureka.pre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.filter.FilterOrder;
import com.example.filter.FilterType;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

// Eureka Pre-route Filter (Redirect)
public class RequestMonitorFilter extends ZuulFilter {
	private Logger logger = LoggerFactory.getLogger( getClass() );
	
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
		return FilterType.PRE;
	}
	
	@Override
	public Object run() {
		final RequestContext ctx = RequestContext.getCurrentContext();
		logger.info( "Request URI : {}", ctx.getRequest().getRequestURI() );
		logger.info( "Request URL : {}", ctx.getRequest().getRequestURL() );
		
		return null;
	}
}
