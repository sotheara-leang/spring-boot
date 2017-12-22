package com.example.springboot.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {

	@Bean
	public CommonsRequestLoggingFilter logFilter() {
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		//filter.setIncludeHeaders(true);
		filter.setIncludePayload(true);
		filter.setAfterMessagePrefix("REQUEST DATA: ");
		return filter;
	}
}
