package com.example.filter;

public interface FilterOrder {
	interface PreRouteOrder {
		final int EurekaRedirectPreFilter = 11;
	}
	
	interface PostRouteOrder {
		final int EurekaRedirectPostFilter = 11;
		final int EurekaErrorRedirectFilter = 12;
	}
	
	interface ErrorOrder {
		// debugging only
		final int ExampleErrorFilter = -1;
	}
}
