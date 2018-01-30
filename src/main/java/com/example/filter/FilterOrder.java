package com.example.filter;

public interface FilterOrder {
	interface PreRouteOrder {
		final int EurekaRedirectPreFilter = 1;
	}
	
	interface PostRouteOrder {
		final int EurekaRedirectPostFilter = 1;
		final int EurekaErrorRedirectFilter = 2;
	}
	
	interface ErrorOrder {
		final int ExampleErrorFilter = -1;
	}
}
