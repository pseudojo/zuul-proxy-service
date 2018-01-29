package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.example.filter.eureka.EurekaResourcePreFilter;
import com.netflix.zuul.ZuulFilter;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ZuulProxyServiceApplication {
	
	private EurekaResourcePreFilter eurekaFilter = null;
	
	public static void main(String[] args) {
		SpringApplication.run(ZuulProxyServiceApplication.class, args);
	}
	
	@Bean
	public ZuulFilter eurekaPreFilter() {
		if (null == eurekaFilter)
			eurekaFilter = new EurekaResourcePreFilter();
		
		return eurekaFilter;
	}
}
