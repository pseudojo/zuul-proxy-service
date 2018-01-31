package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.example.filter.eureka.error.ExampleErrorFilter;
import com.example.filter.eureka.post.EurekaErrorRedirectFilter;
import com.example.filter.eureka.post.EurekaRedirectPostFilter;
import com.example.filter.eureka.pre.RequestMonitorFilter;
import com.netflix.zuul.ZuulFilter;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ZuulProxyServiceApplication {
	private final Logger logger = LoggerFactory.getLogger( getClass() ); 
	
	public static void main(String[] args) {
		SpringApplication.run(ZuulProxyServiceApplication.class, args);
	}
	
	@Bean
	public ZuulFilter requestMonitorFilter() {
		ZuulFilter filter = new RequestMonitorFilter();
		logger.info( "Attach Filter : {}@{}", filter.getClass(), Integer.toHexString( filter.hashCode() ) );
		
		return filter;
	}
	
	@Bean
	public ZuulFilter eurekaPostFilter() {
		ZuulFilter filter = new EurekaRedirectPostFilter();
		logger.info( "Attach Filter : {}@{}", filter.getClass(), Integer.toHexString( filter.hashCode() ) );
		
		return filter;
	}
	
	@Bean
	public ZuulFilter eurekaErrorRedirectFilter() {
		ZuulFilter filter = new EurekaErrorRedirectFilter();
		logger.info( "Attach Filter : {}@{}", filter.getClass(), Integer.toHexString( filter.hashCode() ) );
		
		return filter;
	}
	
	@Bean
	public ZuulFilter exampleErrorFilter() {
		ZuulFilter filter = new ExampleErrorFilter();
		logger.info( "Attach Filter : {}@{}", filter.getClass(), Integer.toHexString( filter.hashCode() ) );
		
		return filter;
	}
}
